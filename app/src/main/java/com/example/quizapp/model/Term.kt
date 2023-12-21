package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Term (
    val id: Int,
    val term: String,
    val definition: String,
    @SerialName(value = "study_set_id")
    val studySetId: Int,
    @SerialName(value = "image_url")
    val imageUrl: String,
    @SerialName(value = "created_at")
    val createdAt: String?,
    @SerialName(value = "updated_at")
    val updatedAt: String?
)