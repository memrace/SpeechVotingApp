package com.northis.speechvotingapp.network

import com.northis.speechvotingapp.model.Speech
import com.northis.speechvotingapp.model.UserVote
import com.northis.speechvotingapp.model.Voting
import kotlinx.coroutines.Deferred
import retrofit2.http.*

private const val SERVICE = "speechvoting/"
private const val VOTING = "votings/"
private const val VOTE = "votes/"
private const val WINNER = "winner/"

interface IVotingService {
    @POST("$SERVICE$VOTING{uuid}/speeches")
    fun addSpeechToVotingAsync(@Path("uuid") votingId: String, @Body speechId: String): Deferred<Unit>

    @POST("$SERVICE$VOTING{uuid}/$VOTE")
    fun addVoteToSpeechAsync(@Path("uuid") votingId: String, @Body vote: UserVote): Deferred<Unit>

    @GET("$SERVICE$VOTING{uuid}")
    fun getVotingAsync(@Path("uuid") votingId: String): Deferred<Voting>

    @GET("$SERVICE$VOTING")
    fun getVotingListAsync(): Deferred<Array<Voting>>

    @DELETE("$SERVICE$VOTING/{uuid}$VOTE{userId}")
    fun removeVoteAsync(@Path("uuid") votingId: String, @Path("userId") userId: String): Deferred<Unit>

    @GET("$SERVICE$VOTING{UUID}/$WINNER")
    fun getWinnerAsync(@Path("uuid") votingId: String): Deferred<Speech>
}