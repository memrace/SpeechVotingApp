package com.northis.speechvotingapp.network

import com.northis.speechvotingapp.model.IdentityUser
import retrofit2.Response
import retrofit2.http.*

private const val SERVICE = "profiles/"
private const val ARRAY = "array/"

interface IProfileService {
    @GET("$SERVICE{uuid}")
    suspend fun getUser(
        @Path("uuid") uuid: String
    ): Response<IdentityUser>

    @GET(SERVICE)
    suspend fun getUsers(): Response<ArrayList<IdentityUser>>

    @POST("$SERVICE$ARRAY")
    suspend fun getUsersArray(@Body arrayId: Array<String>): Response<ArrayList<IdentityUser>>
}