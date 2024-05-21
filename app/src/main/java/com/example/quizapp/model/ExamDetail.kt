package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExamDetail(
    val id: Int,
    @SerialName(value = "test_name")
    val testName: String,
    @SerialName(value = "study_set_id")
    val studySetId: Int,
    val questions: List<Question>,
    @SerialName(value = "created_at")
    val createdAt: String?,
    @SerialName(value = "updated_at")
    val updatedAt: String?
)