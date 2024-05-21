package com.example.quizapp.ui.screens.custom_exam

import com.example.quizapp.model.ExamDetail
import com.example.quizapp.model.ExamResult
import com.example.quizapp.model.Question

data class CustomExamUiState(
    val examDetail: ExamDetail? = null,
    val currentQuestion: Int = 0,
    val currentQuestionResult: ExamResult? = null,
    val isLoading: Boolean = false,
    var questionList: List<Question> = mutableListOf(),
    var examResults: List<ExamResult> = mutableListOf()
)