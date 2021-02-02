package com.northis.speechvotingapp.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.northis.speechvotingapp.authentication.IUserManager
import com.northis.speechvotingapp.model.SpeechStatus
import com.northis.speechvotingapp.model.UserVote
import com.northis.speechvotingapp.model.Voting
import com.northis.speechvotingapp.network.ICatalogService
import com.northis.speechvotingapp.network.IProfileService
import com.northis.speechvotingapp.network.IVotingService
import kotlinx.coroutines.Dispatchers
import java.util.*

// TODO СДЕЛАТЬ ФАСАД, ОБРАБОТКА ОШИБОК.
class VotingViewModel(
    private val context: Context,
    private val votingApi: IVotingService,
    private val catalogApi: ICatalogService,
    private val profileApi: IProfileService,
    private val userManager: IUserManager
) : ViewModel() {
    lateinit var voting: Voting
    val speechId: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    var speechWinnerId: String? = null
    val speechDate: MutableLiveData<Date> by lazy {
        MutableLiveData<Date>()
    }
    private val _userId: String by lazy { userManager.getUserId(context) }

    fun getProfiles() = liveData(Dispatchers.IO) {
        val data = profileApi.getUsers().body()
        emit(data)
    }

    fun getVotingList() = liveData(Dispatchers.IO) {
        val data = votingApi.getVotingList().body()
        Log.d("Votings", data.toString())
        emit(data)
    }

    fun getVoting() = liveData(Dispatchers.IO) {
        val data = votingApi.getVoting(voting.VotingId.toString()).body()
        Log.d("Voting", data.toString())
        emit(data)
    }

    fun getWinner() = liveData(Dispatchers.IO) {
        val data = votingApi.getWinner(voting.VotingId.toString()).body()
        Log.d("Winner", data.toString())
        emit(data)
    }

    fun addSpeechToVoting(speechId: String) = liveData(Dispatchers.IO) {
        val response = votingApi.addSpeech(voting.VotingId.toString(), speechId)
        emit(response)

    }

    fun addVote(speechId: String) = liveData(Dispatchers.IO) {
        val response = votingApi.addVote(voting.VotingId.toString(), UserVote(speechId, _userId))
        emit(response)

    }

    fun removeVote() = liveData(Dispatchers.IO) {
        val response = votingApi.removeVote(voting.VotingId.toString(), _userId)
        emit(response)
    }

    fun switchVote(speechId: String) = liveData(Dispatchers.IO) {
        val response = votingApi.switchVote(voting.VotingId.toString(), _userId, speechId)
        if (response.isSuccessful) {
            emit(response)
        }
    }

    fun loadSpeeches() = liveData(Dispatchers.IO) {
        val data = catalogApi.getSpeeches("theme", status = SpeechStatus.InCatalog.toString()).body()
        Log.d("Speeches", data.toString())
        emit(data)
    }
    fun setExecutor(speechDate: Date) = liveData(Dispatchers.IO){
        if (speechWinnerId != null){
            val data = catalogApi.changeSpeechExecutor(speechWinnerId!!, _userId, speechDate)
            emit(data)
        }

    }
}