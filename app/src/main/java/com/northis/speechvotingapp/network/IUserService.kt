package com.northis.speechvotingapp.network

import com.northis.speechvotingapp.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

private const val SERVICE = "profiles/"
private const val ARRAY = "array/"
interface IUserService {
    @GET("$SERVICE{uuid}")
    fun getUser(
        @Path("uuid") uuid: String
    ): Call<User>
    @GET(SERVICE)
    fun getUsers():Call<Array<User>>

}