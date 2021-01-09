package com.northis.speechvotingapp.di

import android.app.Application
import com.northis.speechvotingapp.di.component.ApiComponent
import com.northis.speechvotingapp.di.component.DaggerApiComponent
import com.northis.speechvotingapp.di.component.DaggerOAuthComponent
import com.northis.speechvotingapp.di.component.OAuthComponent
import com.northis.speechvotingapp.di.module.ApiModule
import com.northis.speechvotingapp.di.module.AppModule
import com.northis.speechvotingapp.di.module.OAuthModule
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*


class App : Application() {
    lateinit var oauthComponent: OAuthComponent
    lateinit var apiComponent: ApiComponent
    override fun onCreate() {
        super.onCreate()
        val oAuthModule = OAuthModule(
            HttpClient(Android) {
                install(JsonFeature) {
                    serializer = GsonSerializer {
                        serializeNulls()
                        disableHtmlEscaping()
                    }
                }
            }
        )
        val appModule = AppModule(this)
        val apiModule = ApiModule("https://192.168.100.8:5002/")
        oauthComponent = DaggerOAuthComponent
            .builder()
            .appModule(appModule)
            .oAuthModule(oAuthModule)
            .build()
        apiComponent = DaggerApiComponent
            .builder()
            .appModule(appModule)
            .oAuthModule(oAuthModule)
            .apiModule(apiModule)
            .build()
    }
}