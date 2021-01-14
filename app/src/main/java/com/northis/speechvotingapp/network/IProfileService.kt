package com.northis.speechvotingapp.network

import com.northis.speechvotingapp.model.User
import kotlinx.coroutines.Deferred
import retrofit2.http.*

private const val SERVICE = "profiles/"
private const val ARRAY = "array/"

interface IProfileService {
    @GET("$SERVICE{uuid}")
    suspend fun getUser(
        @Path("uuid") uuid: String
    ): User

    @GET(SERVICE)
    suspend fun getUsers(): Array<User>

    @POST("$SERVICE$ARRAY")
    suspend fun getUsersArray(@Body arrayId: Array<String>): Array<User>
}