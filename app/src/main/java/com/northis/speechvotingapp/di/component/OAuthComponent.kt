package com.northis.speechvotingapp.di.component

import com.northis.speechvotingapp.di.module.OAuthModule
import com.northis.speechvotingapp.view.authorization.AuthActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [OAuthModule::class])
interface OAuthComponent {
    fun inject(authActivity: AuthActivity)
}