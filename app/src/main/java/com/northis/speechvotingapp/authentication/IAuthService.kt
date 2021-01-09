package com.northis.speechvotingapp.authentication

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IAuthService {
    @POST("token")
    @FormUrlEncoded
    fun getToken(
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("code") code: String,
        @Field("grant_type") grant_type: String,
        @Field("redirect_uri") redirect_uri: String,
        @Field("code_verifier") code_verifier: String
    ): Call<OAuthAccessTokenResponse>

    @POST
    @FormUrlEncoded
    fun refreshToken(
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("grant_type") grant_type: String,
        @Field("refresh_token") refresh_token: String,
    ): Call<OAuthAccessTokenResponse>
}
