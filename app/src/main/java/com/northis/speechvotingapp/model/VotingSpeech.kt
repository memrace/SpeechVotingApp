package com.northis.speechvotingapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VotingSpeech(
    //Todo контракты в gateway менять.
    @SerializedName("SpeechId")
    @Expose
    val SpeechId: Speech,
    @SerializedName("HasUserVoted")
    @Expose
    val HasUserVoted: Boolean,
    @SerializedName("Users")
    @Expose
    val Users: Collection<IdentityUser>
)