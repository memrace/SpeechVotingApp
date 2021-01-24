package com.northis.speechvotingapp.network

import com.northis.speechvotingapp.model.OAuthTokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface IAuthService {
    @POST("token")
    @FormUrlEncoded
    suspend fun getToken(
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("code") code: String,
        @Field("grant_type") grant_type: String,
        @Field("redirect_uri") redirect_uri: String,
        @Field("code_verifier") code_verifier: String
    ): OAuthTokenResponse

    @POST("token")
    @FormUrlEncoded
    suspend fun refreshToken(
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("grant_type") grant_type: String,
        @Field("refresh_token") refresh_token: String,
    ): OAuthTokenResponse
}
