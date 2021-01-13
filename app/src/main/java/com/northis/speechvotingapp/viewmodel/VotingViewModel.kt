package com.northis.speechvotingapp.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.northis.speechvotingapp.model.Speech
import com.northis.speechvotingapp.model.Voting
import com.northis.speechvotingapp.network.ICatalogService
import com.northis.speechvotingapp.network.IProfileService
import com.northis.speechvotingapp.network.IVotingService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class VotingViewModel @Inject constructor(
    private val votingApi: IVotingService,
    private val catalogApi: ICatalogService,
    private val profileApi: IProfileService
) : ViewModel() {

    private val _votingList by lazy { MutableLiveData<Array<Voting>>() }
    private val _voting by lazy { MutableLiveData<Voting>() }
    private val _winner by lazy { MutableLiveData<Speech>() }

    val votingListLiveData: LiveData<Array<Voting>> = _votingList
    val votingLiveData: LiveData<Voting> = _voting
    val winnerLiveData: LiveData<Speech> = _winner
    init {
        loadVotingList()
    }
    private fun loadVotingList() {

            viewModelScope.async {
                val votingList = votingApi.getVotingListAsync().await()
                _votingList.value = votingList
            }
    }

    fun loadVoting(votingId: String){
        viewModelScope.async {
            val voting = votingApi.getVotingAsync(votingId).await()
            _voting.value = voting
        }
    }

    fun loadWinner(votingId: String){
        viewModelScope.async {
            val winner = votingApi.getWinnerAsync(votingId).await()
            _winner.value = winner
        }
    }
}