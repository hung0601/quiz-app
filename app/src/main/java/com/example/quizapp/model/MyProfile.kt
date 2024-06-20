package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MyProfile(
    val id: Int,
    val name: String,
    val email: String,
    @SerialName(value = "image_url")
    val imageUrl: String?,
    @SerialName(value = "courses_count")
    val coursesCount: Int? = 0,
    @SerialName(value = "study_sets_count")
    val studySetsCount: Int? = 0,
    @SerialName(value = "created_at")
    val createdAt: String?,
    @SerialName(value = "updated_at")
    val updatedAt: String?
)