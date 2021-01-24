package com.northis.speechvotingapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.northis.speechvotingapp.authentication.AuthorizationService
import com.northis.speechvotingapp.authentication.IUserManager
import com.northis.speechvotingapp.authentication.OnTokenFailureListener
import com.northis.speechvotingapp.model.SpeechStatus
import com.northis.speechvotingapp.model.UserVote
import com.northis.speechvotingapp.network.ICatalogService
import com.northis.speechvotingapp.network.IProfileService
import com.northis.speechvotingapp.network.IVotingService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


// TODO СДЕЛАТЬ РЕСУРС-КЛАСС, ОБРАБОТКА ОШИБОК.
class VotingViewModel(
    private val context: Context,
    private val votingApi: IVotingService,
    private val catalogApi: ICatalogService,
    private val profileApi: IProfileService,
    private val authService: AuthorizationService,
    private val userManager: IUserManager
) : ViewModel() {
    var votingId: String = ""
    val speechId: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val speechVotingId: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    private val _userId: String by lazy { userManager.getUserId(context) }

    fun getVotingList() = liveData(Dispatchers.IO) {
        val data = votingApi.getVotingList().body()
        emit(data)
    }

    fun getVoting() = liveData(Dispatchers.IO) {
        val data = votingApi.getVoting(votingId)
        emit(data)
    }

    fun getWinner() = liveData(Dispatchers.IO) {
        val data = votingApi.getWinner(votingId)
        emit(data)
    }

    suspend fun addSpeechToVoting(speechId: String) = coroutineScope {
        val data = async(Dispatchers.IO) {
            votingApi.addSpeechToVoting(votingId, speechId)
        }
        data.await()
    }

    suspend fun addVote(speechId: String) = coroutineScope {
        val data = async(Dispatchers.IO) {
            votingApi.addVoteToSpeech(votingId, UserVote(speechId, _userId))
        }
        data.await()
    }

    fun removeVote(userId: String) = liveData(Dispatchers.IO) {
        val data = votingApi.removeVote(votingId, userId)
        emit(data)
    }

    fun loadSpeeches() = liveData(Dispatchers.IO) {
        val data = catalogApi.getSpeeches("theme", status = SpeechStatus.InCatalog.toString())
        emit(data)
    }
}