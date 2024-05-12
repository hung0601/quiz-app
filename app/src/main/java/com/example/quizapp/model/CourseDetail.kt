package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CourseDetail(
    val id: Int,
    val title: String,
    val description: String,
    val owner: Profile,
    val enrollments: List<Enrollment>,
    @SerialName(value = "study_sets")
    val studySets: List<StudySet>,
    @SerialName(value = "created_at")
    val createdAt: String?,
    @SerialName(value = "updated_at")
    val updatedAt: String?,
)