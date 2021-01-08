package com.northis.speechvotingapp.authentication

import com.google.gson.annotations.SerializedName


data class OAuthAccessTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("id_token") val idToken: String,
    @SerializedName("refresh_token") val refreshToken: String? = null,
    @SerializedName("expires_in") val expiresInSeconds: Int? = null,
    @SerializedName("token_type") val tokenType: String? = null,
    @SerializedName("scope") val scopes: String? = null,
)
