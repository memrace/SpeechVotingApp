package com.northis.speechvotingapp.di.module

import com.northis.speechvotingapp.authentication.IOAuthSettingsProvider
import com.northis.speechvotingapp.authentication.IOAuthSettingsProvider.Companion.instance
import com.northis.speechvotingapp.authentication.IUserManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class OAuthModule() {
    @Provides
    @Singleton
    fun provideUserTokenStorage(): IUserManager {
        return IUserManager.instance
    }

    @Provides
    @Singleton
    fun provideOAuthSettings(): IOAuthSettingsProvider {
        return instance
    }

}