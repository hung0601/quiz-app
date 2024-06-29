package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val id: Int,
    val name: String,
    val email: String,
    @SerialName(value = "image_url")
    val imageUrl: String?,
    @SerialName(value = "access_level")
    val accessLevel: Int,
)