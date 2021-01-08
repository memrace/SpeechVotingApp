package com.northis.speechvotingapp.di

import android.app.Application
import com.northis.speechvotingapp.di.component.DaggerOAuthComponent
import com.northis.speechvotingapp.di.component.OAuthComponent
import com.northis.speechvotingapp.di.module.OAuthModule
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*


class App : Application() {
    lateinit var oauthComponent: OAuthComponent
    override fun onCreate() {
        super.onCreate()
        oauthComponent = DaggerOAuthComponent
            .builder()
            .oAuthModule(
                OAuthModule(
                    this,
                    HttpClient(Android) {
                        install(JsonFeature) {
                            serializer = GsonSerializer {
                                serializeNulls()
                                disableHtmlEscaping()
                            }
                        }
                    }
                )).build()
    }
}