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
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Inject
import javax.net.ssl.X509TrustManager


class AuthorizationService @Inject constructor(
    private val context: Context,
    private val userTokenStorage: IUserTokenStorage,
    private val oauthSettingsProvider: IOAuthSettingsProvider
) {

    //CallBack
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

    fun getAccessToken(): String? {
        return if (userTokenStorage.isExpired(context)) {
            GlobalScope.async {
                refreshToken()
            }
            userTokenStorage.getAccessToken(context)
        } else {
            userTokenStorage.getAccessToken(context)
        }
    }

    private fun refreshToken() {
        val refreshToken = userTokenStorage.getRefreshToken(context)
        if (refreshToken != null) {
            val call = getAuthApi().refreshToken(
                oauthSettingsProvider.clientId,
                oauthSettingsProvider.clientSecret,
                oauthSettingsProvider.grantTypeRefresh,
                refreshToken
            )
            call.enqueue(object : Callback<OAuthAccessTokenResponse> {
                override fun onResponse(
                    call: Call<OAuthAccessTokenResponse>,
                    response: Response<OAuthAccessTokenResponse>
                ) {
                    val data = response.body()
                    if (data != null) {
                        with(userTokenStorage) {
                            saveToken(context, data.access_token, data.id_token, data.refresh_token)
                            data.expires_in?.let { setExpirationDate(context, it) }
                        }
                        Log.d(
                            "success",
                            "Токены обновлены и сохранены! ${data.access_token} ; ${data.refresh_token}"
                        )
                    }

                }

                override fun onFailure(call: Call<OAuthAccessTokenResponse>, t: Throwable) {
                    Log.d("error", "Ошибка!!!")
                }

            })
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

    private fun getOAuthData() {
        val call = getAuthApi().getToken(
            oauthSettingsProvider.clientId,
            oauthSettingsProvider.clientSecret,
            responseCode,
            oauthSettingsProvider.grantType,
            oauthSettingsProvider.redirectUri,
            oauthSettingsProvider.codeVerifier
        )
        call.enqueue(object : Callback<OAuthAccessTokenResponse> {
            override fun onResponse(
                call: Call<OAuthAccessTokenResponse>,
                response: Response<OAuthAccessTokenResponse>
            ) {
                val data = response.body()
                with(userTokenStorage) {
                    if (data != null) {
                        saveToken(context, data.access_token, data.id_token, data.refresh_token)
                        data.expires_in?.let {
                            setExpirationDate(context, it)
                        }
                        Log.d(
                            "success",
                            "Токены получены и сохранены! ${data.access_token} ; ${data.refresh_token}"
                        )
                    }
                }
            }

            override fun onFailure(call: Call<OAuthAccessTokenResponse>, t: Throwable) {
                Log.d("error", "Ошибка!!!")
            }

        })

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
                            getOAuthData()
                            callback.onTokenAcquired()
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