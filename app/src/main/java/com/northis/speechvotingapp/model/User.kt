package com.northis.speechvotingapp.model

import java.util.*

data class User(
    val Email: String,
    val FirstName: String,
    val LastName: String,
    val Id: UUID,
    val ImageUrl: String,
    val UserName: String
)
