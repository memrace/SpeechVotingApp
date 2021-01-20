package com.northis.speechvotingapp.di.module

import com.northis.speechvotingapp.authentication.IOAuthSettingsProvider
import com.northis.speechvotingapp.authentication.IOAuthSettingsProvider.Companion.instance
import com.northis.speechvotingapp.authentication.IUserTokenManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class OAuthModule() {
    @Provides
    @Singleton
    fun provideUserTokenStorage(): IUserTokenManager {
        return IUserTokenManager.instance
    }

    @Provides
    @Singleton
    fun provideOAuthSettings(): IOAuthSettingsProvider {
        return instance
    }

}