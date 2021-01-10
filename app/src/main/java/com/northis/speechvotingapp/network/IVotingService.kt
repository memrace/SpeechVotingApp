package com.northis.speechvotingapp.network

import com.northis.speechvotingapp.model.UserVote
import com.northis.speechvotingapp.model.Voting
import retrofit2.Call
import retrofit2.http.*

private const val SERVICE = "speechvoting/"
private const val VOTING = "votings/"
private const val VOTE = "votes/"

interface IVotingService {
    @POST("$SERVICE$VOTING{uuid}/speeches")
    fun addSpeechToVoting(@Path("uuid") votingId: String, @Body speechId: String)

    @POST("$SERVICE$VOTING{uuid}/$VOTE")
    fun addVoteToSpeech(@Path("uuid") votingId: String, @Body vote: UserVote)

    @GET("$SERVICE$VOTING{uuid}")
    fun getVoting(@Path("uuid") votingId: String): Call<Voting>

    @GET("$SERVICE$VOTING")
    fun getVotings(): Call<Array<Voting>>

    @DELETE("$SERVICE$VOTING/{uuid}$VOTE{userId}")
    fun removeVote(@Path("uuid") votingId: String, @Path("userId") userId: String)
}