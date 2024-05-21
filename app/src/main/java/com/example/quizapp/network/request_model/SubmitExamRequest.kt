package com.example.quizapp.network.request_model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitExamRequest(
    @SerialName("question_id")
    var questionId: Int,
    @SerialName("is_correct")
    val isCorrect: Boolean,
    @SerialName("selected_answer")
    val selectedAnswer: String,
)