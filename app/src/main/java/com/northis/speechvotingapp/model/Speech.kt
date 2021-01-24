package com.northis.speechvotingapp.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Speech(
    @SerializedName("SpeechId")
    val SpeechId: UUID,
    @SerializedName("CreateDate")
    val CreateDate: Date,
    @SerializedName("Creator")
    val Creator: IdentityUser,
    @SerializedName("Description")
    val Description: String,
    @SerializedName("Theme")
    val Theme: String,
    @SerializedName("Status")
    val Status: String?,
    @SerializedName("SpeechDate")
    val SpeechDate: Date?,
    @SerializedName("SourceLinks")
    val SourceLinks: String?,
    @SerializedName("Executor")
    val Executor: IdentityUser?
)