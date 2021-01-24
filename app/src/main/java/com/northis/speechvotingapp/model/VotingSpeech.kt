package com.northis.speechvotingapp.model

data class VotingSpeech(
    val Speech: Speech,
    val HasUserVoted: Boolean,
    val Users: ArrayList<IdentityUser>
)