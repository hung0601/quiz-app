package com.example.quizapp.network.request_model

import java.io.File

data class StoreTermRequest(
    val image: File?,
    val term: String,
    val definition: String,
    val studySetId: Int = 0,
)