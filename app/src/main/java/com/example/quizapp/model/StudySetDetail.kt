package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudySetDetail(
    val id: Int,
    val title: String,
    val description: String,
    @SerialName(value = "term_lang")
    val termLang: String,
    @SerialName(value = "def_lang")
    val defLang: String,
    @SerialName(value = "votes_avg_star")
    val votesAvgStar: Float? = null,
    val topics: List<Topic>,
    val terms: List<Term>,
    val exams: List<Exam>,
    val owner: Profile,
    @SerialName(value = "image_url")
    val imageUrl: String?,
    @SerialName(value = "created_at")
    val createdAt: String?,
    @SerialName(value = "updated_at")
    val updatedAt: String?

)