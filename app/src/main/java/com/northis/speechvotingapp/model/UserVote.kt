package com.northis.speechvotingapp.model

import java.util.*

data class UserVote(
    val Speech: UUID,
    val VotingId: UUID
)