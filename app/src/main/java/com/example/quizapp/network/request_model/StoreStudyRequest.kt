package com.example.quizapp.network.request_model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoreStudyRequest(
    @SerialName("term_id")
    var termId: Int,
    @SerialName("is_correct")
    val isCorrect: Boolean,
)