package com.example.quizapp.model

import com.example.quizapp.constants.PUBLIC_ACCESS
import com.example.quizapp.constants.VIEW_ACCESS_LEVEL
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
    @SerialName(value = "access_type")
    val accessType: Int = PUBLIC_ACCESS,
    @SerialName(value = "votes_avg_star")
    val votesAvgStar: Float? = null,
    val permission: Int = VIEW_ACCESS_LEVEL,
    val topics: List<Topic>,
    val terms: List<Term>,
    val exams: List<Exam>,
    val owner: MyProfile,
    @SerialName(value = "image_url")
    val imageUrl: String?,
    @SerialName(value = "created_at")
    val createdAt: String?,
    @SerialName(value = "updated_at")
    val updatedAt: String?
)