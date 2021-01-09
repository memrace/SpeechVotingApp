package com.northis.speechvotingapp.model

import java.util.*

data class Speech(
    val SpeechId: UUID,
    val CreateDate: Date,
    val Creator: User,
    val Description: String,
    val Theme: String,
    val Status: String?,
    val SpeechDate: Date?,
    val SourceLinks: String?,
    val Executor: User?
)