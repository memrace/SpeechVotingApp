package com.northis.speechvotingapp.di.component

import com.northis.speechvotingapp.di.module.ApiModule
import com.northis.speechvotingapp.di.module.AppModule
import com.northis.speechvotingapp.di.module.OAuthModule
import com.northis.speechvotingapp.view.catalog.CatalogActivity
import com.northis.speechvotingapp.view.schedule.ScheduleActivity
import com.northis.speechvotingapp.view.voting.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiModule::class, OAuthModule::class])
interface ApiComponent {
    fun inject(votingActivity: VotingActivity)
    fun inject(catalogActivity: CatalogActivity)
    fun inject(scheduleActivity: ScheduleActivity)
    fun inject(votingMainFragment: VotingMainFragment)
    fun inject(votingDetailsFragment: VotingDetailsFragment)
    fun inject(votingDetailsFragment: VotingAddSpeechFragment)
    fun inject(votingDetailsFragment: VotingAddScheduleFragment)
}