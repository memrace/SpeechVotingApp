package com.northis.speechvotingapp.authentication

data class OAuthAccessTokenResponse(
    val access_token: String,
    val id_token: String,
    val refresh_token: String? = null,
    val expires_in: Int? = null,
    val token_type: String? = null,
    val scope: String? = null,
)
