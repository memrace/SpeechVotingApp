package com.northis.speechvotingapp.model
import java.util.*

data class Voting(
    val Creator: IdentityUser,
    val EndDate: Date?,
    val HasUserVoted: Boolean?,
    val StartDate: Date?,
    val Title: String,
    val TotalVotes: Int?,
    val VotingId: UUID,
    val VotingSpeeches: List<VotingSpeech>?
)

