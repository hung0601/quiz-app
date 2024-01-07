package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudySetDetail(
    val id: Int,
    val title: String,
    val description: String,
    val terms: List<Term>,
    val owner: Profile,
    @SerialName(value = "image_url")
    val imageUrl: String,
    @SerialName(value = "created_at")
    val createdAt: String?,
    @SerialName(value = "updated_at")
    val updatedAt: String?

)