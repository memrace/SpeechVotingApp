package com.northis.speechvotingapp.model

data class VotingSpeech(
    //Todo контракты в gateway менять.
    val SpeechId: Speech,
    val HasUserVoted: Boolean,
    val Users: Collection<User>
)