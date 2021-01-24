package com.northis.speechvotingapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.northis.speechvotingapp.authentication.AuthorizationService
import com.northis.speechvotingapp.authentication.IUserManager
import com.northis.speechvotingapp.network.ICatalogService
import com.northis.speechvotingapp.network.IProfileService
import com.northis.speechvotingapp.network.IVotingService
import javax.inject.Inject

class VotingViewModelFactory @Inject constructor(
    private val application: Application,
    private val votingApi: IVotingService,
    private val catalogApi: ICatalogService,
    private val profileApi: IProfileService,
    private val authService: AuthorizationService,
    private val userManager: IUserManager
) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VotingViewModel(application, votingApi, catalogApi, profileApi, authService, userManager) as T
    }
}