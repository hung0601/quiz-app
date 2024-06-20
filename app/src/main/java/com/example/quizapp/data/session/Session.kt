package com.example.quizapp.data.session

import com.example.quizapp.model.MyProfile

data class Session(
    val token: String,
    val expiresAt: Long,
    val user: MyProfile
)