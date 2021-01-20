package com.northis.speechvotingapp.authentication


import android.app.Application
import android.content.Context
import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.northis.speechvotingapp.network.IAuthService
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Inject
import javax.net.ssl.X509TrustManager


class AuthorizationService @Inject constructor(
    private val context: Context,
    private val application: Application,
    private val userTokenManager: IUserTokenManager,
    private val oauthSettingsProvider: IOAuthSettingsProvider
) {

    // CallBack
    private lateinit var callback: OnTokenAcquiredListener

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

    public suspend fun checkAuthorization(callback: OnTokenFailureListener){
        val accessToken = userTokenManager.getAccessToken(context)
        val refreshToken = userTokenManager.getRefreshToken(context)
        if (accessToken == null || refreshToken == null) {
            callback.onTokenFailure()
        } else {
            if (userTokenManager.isExpiredToken(context)){
                val data = GlobalScope.async(Dispatchers.IO) {
                    refreshToken()
                }
                data.await()
            }
        }
    }


    private fun getAuthApi(): IAuthService {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        val client = OkHttpClient.Builder()
        with(client) {
            sslSocketFactory(
                UnsafeConnection.getSslSocketFactory(),
                UnsafeConnection.getTrustAllCerts()[0] as X509TrustManager
            )
            hostnameVerifier { _, _ -> true }
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(oauthSettingsProvider.tokenUrl)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .build()
        return retrofit.create(IAuthService::class.java)
    }


    private suspend fun refreshToken() = coroutineScope {
        val refreshToken = userTokenManager.getRefreshToken(context)
        if (refreshToken != null) {
            val data = async(Dispatchers.IO) {
                getAuthApi().refreshToken(
                    oauthSettingsProvider.clientId,
                    oauthSettingsProvider.clientSecret,
                    oauthSettingsProvider.grantTypeRefresh,
                    refreshToken
                )
            }
            val oauthData = data.await()
            with(userTokenManager) {
                saveToken(
                    context,
                    oauthData.access_token,
                    oauthData.id_token,
                    oauthData.refresh_token
                )
                oauthData.expires_in?.let { setExpirationDate(context, it) }
            }
            Log.d(
                "success",
                "Токены обновлены и сохранены! ${oauthData.access_token} ; ${oauthData.refresh_token}"
            )
        }
    }

    private suspend fun getOAuthData() = coroutineScope {

        val data = async(Dispatchers.IO) {
            getAuthApi().getToken(
                oauthSettingsProvider.clientId,
                oauthSettingsProvider.clientSecret,
                responseCode,
                oauthSettingsProvider.grantType,
                oauthSettingsProvider.redirectUri,
                oauthSettingsProvider.codeVerifier
            )
        }
        val oauthData = data.await()
        with(userTokenManager) {
            saveToken(
                context,
                oauthData.access_token,
                oauthData.id_token,
                oauthData.refresh_token
            )
            oauthData.expires_in?.let {
                setExpirationDate(context, it)
            }
            Log.d(
                "success",
                "Токены получены и сохранены! ${oauthData.access_token} ; ${oauthData.refresh_token}"
            )
            callback.onTokenAcquired()
        }
    }

    fun startAuthentication(webView: WebView, callback: OnTokenAcquiredListener) {
        this.callback = callback
        webView.settings.javaScriptEnabled = true
        // TODO Убрать когда будут сертификаты.
        webView.webViewClient = WvClient()
        webView.settings.useWideViewPort = true
        webView.loadUrl(uri.toString())
        Log.d("Request Access Token", "Запрос на получение токена.")
    }

    private inner class WvClient : WebViewClient() {
        // TODO Убрать когда будут сертификаты.
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed()
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
                            Log.d("code", responseCode)
                            GlobalScope.launch { getOAuthData() }

                        } ?: run {
                            Log.d("problems", "что-то пошло не так.")
                        }
                    }
                }
            }
            return super.shouldOverrideUrlLoading(view, request)
        }
    }
}