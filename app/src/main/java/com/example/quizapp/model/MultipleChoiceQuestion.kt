package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MultipleChoiceQuestion(
    val id: Int = 0,
    val question: String,
    val answers: List<String>,
    @SerialName(value = "correct_answer")
    val correctAnswer: Int,
)