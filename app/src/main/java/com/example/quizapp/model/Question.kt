package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class Question(
    val id: Int = 0,
    @SerialName(value = "term_referent_id")
    val termReferentId: Int? = 0,
    @SerialName(value = "has_audio")
    val hasAudio: Boolean = false,
    @SerialName(value = "audio_text")
    val audioText: String? = null,
    @SerialName(value = "audio_lang")
    val audioLang: String? = null,
    @SerialName(value = "question_type")
    val questionType: String,
    val question: JsonObject,
)