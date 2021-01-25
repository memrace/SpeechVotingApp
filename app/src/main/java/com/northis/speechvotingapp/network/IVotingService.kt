package com.northis.speechvotingapp.network

import com.northis.speechvotingapp.model.Speech
import com.northis.speechvotingapp.model.UserVote
import com.northis.speechvotingapp.model.Voting
import retrofit2.Response
import retrofit2.http.*

private const val SERVICE = "speechvoting/"
private const val VOTING = "votings/"
private const val VOTE = "votes/"
private const val WINNER = "winner/"

interface IVotingService {
    @POST("$SERVICE$VOTING{uuid}/speeches")
    suspend fun addSpeech(@Path("uuid") votingId: String, @Body speechId: String): Response<Unit>

    @POST("$SERVICE$VOTING{uuid}/$VOTE")
    suspend fun addVote(@Path("uuid") votingId: String, @Body vote: UserVote): Response<Unit>

    @GET("$SERVICE$VOTING{uuid}")
    suspend fun getVoting(@Path("uuid") votingId: String): Response<Voting>

    @GET("$SERVICE$VOTING")
    suspend fun getVotingList(): Response<ArrayList<Voting>>

    @DELETE("$SERVICE$VOTING{uuid}/$VOTE{userId}")
    suspend fun removeVote(@Path("uuid") votingId: String, @Path("userId") userId: String): Response<Unit>

    @GET("$SERVICE$VOTING{uuid}/$WINNER")
    suspend fun getWinner(@Path("uuid") votingId: String): Response<Speech>

    @PATCH("$SERVICE$VOTING{uuid}/$VOTE{userId}")
    suspend fun switchVote(@Path("uuid") votingId: String, @Path("userId") userId: String, @Body speechId: String): Response<Unit>

}