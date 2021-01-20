package com.northis.speechvotingapp.network

import com.northis.speechvotingapp.model.Speech
import com.northis.speechvotingapp.model.UserVote
import com.northis.speechvotingapp.model.Voting
import retrofit2.http.*

private const val SERVICE = "speechvoting/"
private const val VOTING = "votings/"
private const val VOTE = "votes/"
private const val WINNER = "winner/"

interface IVotingService {
    @POST("$SERVICE$VOTING{uuid}/speeches")
    suspend fun addSpeechToVoting(@Path("uuid") votingId: String, @Body speechId: String)

    @POST("$SERVICE$VOTING{uuid}/$VOTE")
    suspend fun addVoteToSpeech(@Path("uuid") votingId: String, @Body vote: UserVote)

    @GET("$SERVICE$VOTING{uuid}")
    suspend fun getVoting(@Path("uuid") votingId: String): Voting

    @GET("$SERVICE$VOTING")
    suspend fun getVotingList(): ArrayList<Voting>

    @DELETE("$SERVICE$VOTING/{uuid}$VOTE{userId}")
    suspend fun removeVote(@Path("uuid") votingId: String, @Path("userId") userId: String)

    @GET("$SERVICE$VOTING{UUID}/$WINNER")
    suspend fun getWinner(@Path("uuid") votingId: String): Speech
}