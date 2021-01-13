package com.northis.speechvotingapp.network

import com.northis.speechvotingapp.model.User
import kotlinx.coroutines.Deferred
import retrofit2.http.*

private const val SERVICE = "profiles/"
private const val ARRAY = "array/"

interface IProfileService {
    @GET("$SERVICE{uuid}")
    fun getUser(
        @Path("uuid") uuid: String
    ): Deferred<User>

    @GET(SERVICE)
    fun getUsers(): Deferred<Array<User>>

    @POST("$SERVICE$ARRAY")
    fun getUsersArray(@Body arrayId: Array<String>): Deferred<Array<User>>
}