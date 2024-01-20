package com.example.quizapp.data.session

import com.example.quizapp.model.Profile

data class Session(
    val token: String,
    val expiresAt: Long,
    val user: Profile
)