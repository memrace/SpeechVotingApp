package com.northis.speechvotingapp.authentication


import android.content.Context
import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


class AuthorizationService @Inject constructor(
    private val context: Context,
    private val userTokenStorage: IUserTokenStorage,
    private val oauthSettingsProvide: IOAuthSettingsProvider,
    private val client: HttpClient
) {

    // Создаем и проверяем в ответе для избежания CSRF атак.
    private val uniqueState = UUID.randomUUID().toString()

    private lateinit var responseCode: String;

    // Подготавливаем URL
    private val uri = Uri.parse(oauthSettingsProvide.authUrl)
        .buildUpon()
        .appendQueryParameter("client_id", oauthSettingsProvide.clientId)
        .appendQueryParameter("code_challenge_method", "S256")
        .appendQueryParameter("code_challenge", oauthSettingsProvide.codeChallenge)
        .appendQueryParameter("redirect_uri", oauthSettingsProvide.redirectUri)
        .appendQueryParameter("response_type", oauthSettingsProvide.responseType)
        .appendQueryParameter("scope", oauthSettingsProvide.scope)
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

    private suspend fun refreshToken() = coroutineScope {
        val refreshToken = userTokenStorage.getRefreshToken(context)
        if (refreshToken != null) {
            val data = client.post<OAuthAccessTokenResponse>(oauthSettingsProvide.tokenUrl) {
                body = FormDataContent(Parameters.build {
                    append("client_id", oauthSettingsProvide.clientId)
                    append("client_secret", oauthSettingsProvide.clientSecret)
                    append("grant_type", oauthSettingsProvide.grantTypeRefresh)
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

    fun beginAuthentication(webView: WebView) {
        webView.settings.javaScriptEnabled = true
        // TODO Убрать когда будут сертификаты.
        webView.webViewClient = WvClient()
        webView.loadUrl(uri.toString())
        Log.d("Request Access Token", "Запрос на получение токена.")
    }

    private suspend fun getOAuthData() = coroutineScope {
        val data = client.post<OAuthAccessTokenResponse>(oauthSettingsProvide.tokenUrl) {
            body = FormDataContent(Parameters.build {
                append("client_id", oauthSettingsProvide.clientId)
                append("client_secret", oauthSettingsProvide.clientSecret)
                append("code", responseCode)
                append("grant_type", oauthSettingsProvide.grantType)
                append("redirect_uri", oauthSettingsProvide.redirectUri)
                append("code_verifier", oauthSettingsProvide.codeVerifier)
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
                if (request.url.toString().startsWith(oauthSettingsProvide.redirectUri)) {
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