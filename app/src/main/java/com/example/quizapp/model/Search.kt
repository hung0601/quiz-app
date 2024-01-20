package com.example.quizapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Search(
    val courses: List<Course>,
    @SerialName(value = "study_sets")
    val studySets: List<StudySet>,
    val creators: List<Profile>
)