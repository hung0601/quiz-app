package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Exam(
    val id: Int,
    @SerialName(value = "test_name")
    val testName: String,
    @SerialName(value = "question_count")
    val questionCount: Int,
    @SerialName(value = "created_at")
    val createdAt: String?,
    @SerialName(value = "updated_at")
    val updatedAt: String?
)