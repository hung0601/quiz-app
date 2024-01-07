package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Course(
    val id: Int,
    val title: String,
    val description: String,
    val owner: Profile,
    @SerialName(value = "enrollments_count")
    val enrollmentsCount: Int,
    @SerialName(value = "study_sets_count")
    val studySetCount: Int,
    @SerialName(value = "created_at")
    val createdAt: String?,
    @SerialName(value = "updated_at")
    val updatedAt: String?
)