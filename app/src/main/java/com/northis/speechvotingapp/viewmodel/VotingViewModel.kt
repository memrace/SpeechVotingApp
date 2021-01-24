package com.northis.speechvotingapp.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.northis.speechvotingapp.authentication.AuthorizationService
import com.northis.speechvotingapp.authentication.IUserManager
import com.northis.speechvotingapp.model.SpeechStatus
import com.northis.speechvotingapp.model.UserVote
import com.northis.speechvotingapp.network.ICatalogService
import com.northis.speechvotingapp.network.IProfileService
import com.northis.speechvotingapp.network.IVotingService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope


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
        val data = votingApi.getVoting(votingId).body()
        emit(data)
    }

    fun getWinner() = liveData(Dispatchers.IO) {
        val data = votingApi.getWinner(votingId).body()
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
            votingApi.addVoteToSpeech(votingId, UserVote(speechId, _userId)).body()
        }
        data.await()
    }

    fun removeVote(userId: String) = liveData(Dispatchers.IO) {
        val data = votingApi.removeVote(votingId, userId).body()
        emit(data)
    }

    fun loadSpeeches() = liveData(Dispatchers.IO) {
        val data =
            catalogApi.getSpeeches("theme", status = SpeechStatus.InCatalog.toString()).body()
        emit(data)
    }
}