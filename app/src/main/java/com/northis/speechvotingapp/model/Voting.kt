package com.northis.speechvotingapp.model

import java.util.*

data class Voting(
    val VotingId: UUID,
    val Title: String,
    val Creator: User,
    val TotalVotes: Int?,
    val EndDate: Date?,
    val StartDate: Date?,
    val HasUserVoted: Boolean?,
    val VotingSpeeches: Collection<Speech>?
)