package com.northis.speechvotingapp.model

data class OAuthTokenResponse(
    val access_token: String,
    val id_token: String,
    val refresh_token: String?,
    val expires_in: Long?,
    val token_type: String?,
    val scope: String?,
)
