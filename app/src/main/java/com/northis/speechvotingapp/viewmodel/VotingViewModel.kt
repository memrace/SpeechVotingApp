package com.northis.speechvotingapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.northis.speechvotingapp.authentication.AuthorizationService
import com.northis.speechvotingapp.authentication.IUserManager
import com.northis.speechvotingapp.model.SpeechStatus
import com.northis.speechvotingapp.model.UserVote
import com.northis.speechvotingapp.model.Voting
import com.northis.speechvotingapp.model.VotingSpeech
import com.northis.speechvotingapp.network.ICatalogService
import com.northis.speechvotingapp.network.IProfileService
import com.northis.speechvotingapp.network.IVotingService
import kotlinx.coroutines.Dispatchers


// TODO СДЕЛАТЬ РЕСУРС-КЛАСС, ОБРАБОТКА ОШИБОК.
class VotingViewModel(
    private val context: Context,
    private val votingApi: IVotingService,
    private val catalogApi: ICatalogService,
    private val profileApi: IProfileService,
    private val authService: AuthorizationService,
    private val userManager: IUserManager
) : ViewModel() {
    var voting: Voting? = null
    val speechId: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val _userId: String by lazy { userManager.getUserId(context) }

    fun getVotingList() = liveData(Dispatchers.IO) {
        val data = votingApi.getVotingList().body()
        emit(data)
    }

    fun getVoting() = liveData(Dispatchers.IO) {
        val data = votingApi.getVoting(voting?.VotingId.toString()).body()
        emit(data)
    }

    fun getWinner() = liveData(Dispatchers.IO) {
        val data = votingApi.getWinner(voting?.VotingId.toString()).body()
        emit(data)
    }

    fun addSpeechToVoting(speechId: String) = liveData(Dispatchers.IO) {
        val response = votingApi.addSpeech(voting?.VotingId.toString(), speechId)
        if (response.isSuccessful) {
            emit(response)
        }
    }

    fun addVote(speechId: String) = liveData(Dispatchers.IO) {
        val response = votingApi.addVote(voting?.VotingId.toString(), UserVote(speechId, _userId))
        if (response.isSuccessful) {
            emit(response)
        }
    }

    fun removeVote() = liveData(Dispatchers.IO) {
        val response = votingApi.removeVote(voting?.VotingId.toString(), _userId)
        emit(response)
    }

    fun switchVote(speechId: String) = liveData(Dispatchers.IO) {
        val response = votingApi.switchVote(voting?.VotingId.toString(), _userId, speechId)
        if (response.isSuccessful){
            emit(response)
        }
    }

    fun loadSpeeches() = liveData(Dispatchers.IO) {
        val data = catalogApi.getSpeeches("theme", status = SpeechStatus.InCatalog.toString()).body()
        emit(data)
    }
}