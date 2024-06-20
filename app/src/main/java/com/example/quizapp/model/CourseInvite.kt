package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseInvite(
    val owner: MyProfile,
    @SerialName(value = "course_id")
    val courseId: Int,
    @SerialName(value = "course_title")
    val courseTitle: String,
//    @SerialName(value = "created_at")
//    val createdAt: String?,
//    @SerialName(value = "updated_at")
//    val updatedAt: String?
)