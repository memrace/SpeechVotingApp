package com.northis.speechvotingapp.network

import com.northis.speechvotingapp.model.IdentityUser
import retrofit2.http.*

private const val SERVICE = "profiles/"
private const val ARRAY = "array/"

interface IProfileService {
    @GET("$SERVICE{uuid}")
    suspend fun getUser(
        @Path("uuid") uuid: String
    ): IdentityUser

    @GET(SERVICE)
    suspend fun getUsers(): ArrayList<IdentityUser>

    @POST("$SERVICE$ARRAY")
    suspend fun getUsersArray(@Body arrayId: Array<String>): ArrayList<IdentityUser>
}