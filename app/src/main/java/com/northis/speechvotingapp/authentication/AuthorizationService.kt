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
import kotlinx.coroutines.*
import java.util.*

private const val AUTH_URL: String = "http://192.168.100.8:5001/connect/authorize"
private const val TOKEN_URL: String = "http://192.168.100.8:5001/connect/token"
private const val CLIENT_ID: String = "SpeechVotingAndroid"
private const val CLIENT_SECRET: String = "androidSecret"
private const val GRANT_TYPE: String = "authorization_code"
private const val GRANT_TYPE_REFRESH: String = "refresh_token"
private const val REDIRECT_URI: String = "https://com.northis.speechvotingapp/signin-oidc"
private const val RESPONSE_TYPE: String = "code"
private const val SCOPE: String = "SpeechVotingApi openid profile offline_access"

/**
 * PKCE
 */
//TODO Генераторы.
private const val CODE_CHALLENGE: String = "oTsyb5tdGTz6jcjq6eh5CJdMMZrnVdEoGOkiy0GnJek"
private const val CODE_VERIFIER: String = "NRaxVF6qEMcF-Kc_aL3VQfxup5cmc43xL7N8tUs313w"

class AuthorizationService(
    private val context: Context,
    private val userTokenStorage: IUserTokenStorage,
    private val webView: WebView,
    private val client: HttpClient
) {

    // Создаем и проверяем в ответе для избежания CSRF атак.
    private val uniqueState = UUID.randomUUID().toString()

    private lateinit var responseCode: String;

    // Подготавливаем URL
    private val uri = Uri.parse(AUTH_URL)
        .buildUpon()
        .appendQueryParameter("client_id", CLIENT_ID)
        .appendQueryParameter("code_challenge_method", "S256")
        .appendQueryParameter("code_challenge", CODE_CHALLENGE)
        .appendQueryParameter("redirect_uri", REDIRECT_URI)
        .appendQueryParameter("response_type", RESPONSE_TYPE)
        .appendQueryParameter("scope", SCOPE)
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
            val data = client.post<OAuthAccessTokenResponse>(TOKEN_URL) {
                body = FormDataContent(Parameters.build {
                    append("client_id", CLIENT_ID)
                    append("client_secret", CLIENT_SECRET)
                    append("grant_type", GRANT_TYPE_REFRESH)
                    append("refresh_token", refreshToken)
                })
            }
            with(userTokenStorage) {
                saveToken(context, data.accessToken, data.idToken, data.refreshToken)
                data.expiresInSeconds?.let { setExpirationDate(context, it) }
            }
            Log.d("success", "Токены обновлены и сохранены!")
        }

    }

    fun beginAuthentication() {
        webView.settings.javaScriptEnabled = true
        // TODO Убрать когда будут сертификаты.
        webView.webViewClient = WvClient()
        webView.loadUrl(uri.toString())
        // TODO Убрать.
        Log.d("Request Access Token", "Запрос на получение токена.")
    }

    private suspend fun getOAuthData() = coroutineScope {
        val data = client.post<OAuthAccessTokenResponse>(TOKEN_URL) {
            body = FormDataContent(Parameters.build {
                append("client_id", CLIENT_ID)
                append("client_secret", CLIENT_SECRET)
                append("code", responseCode)
                append("grant_type", GRANT_TYPE)
                append("redirect_uri", REDIRECT_URI)
                append("code_verifier", CODE_VERIFIER)
            })
        }
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
                if (request.url.toString().startsWith(REDIRECT_URI)) {
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