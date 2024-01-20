package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Enrollment(
    val user: Profile,
    @SerialName(value = "created_at")
    val createdAt: String?,
    @SerialName(value = "updated_at")
    val updatedAt: String?
)