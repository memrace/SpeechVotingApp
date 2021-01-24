package com.northis.speechvotingapp.model
import java.util.*
import kotlin.collections.ArrayList

data class Voting(
    val Creator: IdentityUser,
    val EndDate: Date?,
    val HasUserVoted: Boolean?,
    val StartDate: Date?,
    val Title: String,
    val TotalVotes: Int?,
    val VotingId: UUID,
    val VotingSpeeches: ArrayList<VotingSpeech>?
)

