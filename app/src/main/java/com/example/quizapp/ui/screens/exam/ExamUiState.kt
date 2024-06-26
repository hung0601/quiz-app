package com.example.quizapp.ui.screens.exam

import com.example.quizapp.model.ExamResult
import com.example.quizapp.model.Question
import com.example.quizapp.model.StudySetDetail

data class ExamUiState(
    val studySetDetail: StudySetDetail,
    val currentTerm: Int = 0,
    val currentQuestionResult: ExamResult? = null,
    val isLoading: Boolean = false,
    var questionList: List<Question> = mutableListOf(),
    var examResults: List<ExamResult> = mutableListOf()
)