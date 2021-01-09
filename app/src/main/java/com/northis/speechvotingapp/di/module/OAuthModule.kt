package com.northis.speechvotingapp.di.module

import com.northis.speechvotingapp.authentication.IOAuthSettingsProvider
import com.northis.speechvotingapp.authentication.IOAuthSettingsProvider.Companion.instance
import com.northis.speechvotingapp.authentication.IUserTokenStorage
import dagger.Module
import dagger.Provides
import io.ktor.client.*
import javax.inject.Singleton

@Module
class OAuthModule(
    private val httpClient: HttpClient
) {
    @Provides
    @Singleton
    fun provideUserTokenStorage(): IUserTokenStorage {
        return IUserTokenStorage.instance
    }

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return httpClient;
    }

    @Provides
    @Singleton
    fun provideOAuthSettings(): IOAuthSettingsProvider {
        return instance
    }
}