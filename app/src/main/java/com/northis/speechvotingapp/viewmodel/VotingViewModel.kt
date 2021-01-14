package com.northis.speechvotingapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.northis.speechvotingapp.model.UserVote
import com.northis.speechvotingapp.network.ICatalogService
import com.northis.speechvotingapp.network.IProfileService
import com.northis.speechvotingapp.network.IVotingService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


// TODO СДЕЛАТЬ РЕСУРС-КЛАСС, ОБРАБОТКА ОШИБОК.
class VotingViewModel @Inject constructor(
    private val votingApi: IVotingService,
    private val catalogApi: ICatalogService,
    private val profileApi: IProfileService
) : ViewModel() {


    fun getVotingList() = liveData(Dispatchers.IO) {
        val data = votingApi.getVotingList()
        emit(data)
    }

    fun getVoting(votingId: String) = liveData(Dispatchers.IO) {
        val data = votingApi.getVoting(votingId)
        emit(data)
    }

    fun getWinner(votingId: String) = liveData(Dispatchers.IO) {
        val data = votingApi.getWinner(votingId)
        emit(data)
    }

    fun addSpeechToVoting(votingId: String, speechId: String) = liveData(Dispatchers.IO) {
        val data = votingApi.addSpeechToVoting(votingId, speechId)
        emit(data)
    }

    fun addVoteToSpeech(votingId: String, vote: UserVote) = liveData(Dispatchers.IO) {
        val data = votingApi.addVoteToSpeech(votingId, vote)
        emit(data)
    }

    fun removeVote(votingId: String, userId: String) = liveData(Dispatchers.IO) {
        val data = votingApi.removeVote(votingId, userId)
        emit(data)
    }
}