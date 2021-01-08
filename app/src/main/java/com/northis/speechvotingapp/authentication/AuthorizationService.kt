package com.northis.speechvotingapp.authentication


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.northis.speechvotingapp.view.voting.VotingActivity
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


class AuthorizationService @Inject constructor(
    private val context: Context,
    private val userTokenStorage: IUserTokenStorage,
    private val oauthSettingsProvider: IOAuthSettingsProvider,
    private val client: HttpClient
) {
    // Создаем и проверяем в ответе для избежания CSRF атак.
    private val uniqueState = UUID.randomUUID().toString()

    private lateinit var responseCode: String;

    // Подготавливаем URL
    private val uri = Uri.parse(oauthSettingsProvider.authUrl)
        .buildUpon()
        .appendQueryParameter("client_id", oauthSettingsProvider.clientId)
        .appendQueryParameter("code_challenge_method", "S256")
        .appendQueryParameter("code_challenge", oauthSettingsProvider.codeChallenge)
        .appendQueryParameter("redirect_uri", oauthSettingsProvider.redirectUri)
        .appendQueryParameter("response_type", oauthSettingsProvider.responseType)
        .appendQueryParameter("scope", oauthSettingsProvider.scope)
        .appendQueryParameter("state", uniqueState)
        .build()

    fun getAccessToken(): String? {
        return if (userTokenStorage.isExpired(context)) {
            GlobalScope.launch {
                refreshToken()
            }
            userTokenStorage.getAccessToken(context)
        } else {
            userTokenStorage.getAccessToken(context)
        }
    }

    fun returnResult(act: Activity) {
        act.setResult(RESULT_OK, Intent(context, VotingActivity::class.java))
        act.finish()
    }

    private suspend fun refreshToken() = coroutineScope {
        val refreshToken = userTokenStorage.getRefreshToken(context)
        if (refreshToken != null) {
            val data = client.post<OAuthAccessTokenResponse>(oauthSettingsProvider.tokenUrl) {
                body = FormDataContent(Parameters.build {
                    append("client_id", oauthSettingsProvider.clientId)
                    append("client_secret", oauthSettingsProvider.clientSecret)
                    append("grant_type", oauthSettingsProvider.grantTypeRefresh)
                    append("refresh_token", refreshToken)
                })
            }
            client.close()
            with(userTokenStorage) {
                saveToken(context, data.accessToken, data.idToken, data.refreshToken)
                data.expiresInSeconds?.let { setExpirationDate(context, it) }
            }
            Log.d("success", "Токены обновлены и сохранены!")
        }

    }

    fun startAuthentication(webView: WebView, callback: OnTokenAcquiredListener) {
        webView.settings.javaScriptEnabled = true
        // TODO Убрать когда будут сертификаты.
        webView.webViewClient = WvClient()
        webView.loadUrl(uri.toString())
        Log.d("Request Access Token", "Запрос на получение токена.")
        callback.onTokenAcquired()
    }

    private suspend fun getOAuthData() = coroutineScope {
        val data = client.post<OAuthAccessTokenResponse>(oauthSettingsProvider.tokenUrl) {
            body = FormDataContent(Parameters.build {
                append("client_id", oauthSettingsProvider.clientId)
                append("client_secret", oauthSettingsProvider.clientSecret)
                append("code", responseCode)
                append("grant_type", oauthSettingsProvider.grantType)
                append("redirect_uri", oauthSettingsProvider.redirectUri)
                append("code_verifier", oauthSettingsProvider.codeVerifier)
            })
        }
        client.close()
        with(userTokenStorage) {
            saveToken(context, data.accessToken, data.idToken, data.refreshToken)
            data.expiresInSeconds?.let { setExpirationDate(context, it) }
        }
        Log.d("success", "Токены получены и сохранены!")
    }

    private inner class WvClient : WebViewClient() {
        // TODO Убрать когда будут сертификаты.
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed()
            super.onReceivedSslError(view, handler, error)
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view?.clearCache(true)
            request?.let {
                if (request.url.toString().startsWith(oauthSettingsProvider.redirectUri)) {
                    var responseState = request.url.getQueryParameter("state")
                    if (responseState == uniqueState) {
                        request?.url.getQueryParameter("code")?.let { code ->
                            Log.d(
                                "Success",
                                "Авторизация прошла успешно."
                            )
                            responseCode = code
                            view?.visibility = View.GONE
                            GlobalScope.launch {
                                getOAuthData()
                            }
                        } ?: run {
                            // Пользователь сбросил авторизацию.
                            // TODO Обработку ошибок.
                        }
                    }
                }
            }

            return super.shouldOverrideUrlLoading(view, request)
        }
    }
}