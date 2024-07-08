package com.example.quizapp.network.request_model

import com.example.quizapp.constants.PUBLIC_ACCESS
import java.io.File

data class StoreStudySetRequest(
    var image: File? = null,
    var title: String = "",
    var description: String = "",
    var accessType: Int = PUBLIC_ACCESS,
    var courseId: Int? = null,
    var termLang: String = "",
    var defLang: String = "",
    var topicIds: List<Int> = emptyList()
)