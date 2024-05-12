package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TypeAnswerQuestion(
    val id: Int = 0,
    val question: String,
    @SerialName(value = "correct_answer")
    val correctAnswer: String,
)