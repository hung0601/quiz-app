package com.example.quizapp.ui.screens.exam

import com.example.quizapp.model.Question

data class ExamUiState(
    val currentTerm: Int = 0,
    val currentQuestionResult: ExamResult? = null,
    val isLoading: Boolean = false,
    var questionList: List<Question> = mutableListOf(),
    var examResults: List<ExamResult> = mutableListOf()
)

data class ExamResult(
    val question: Question,
    val questionDetail: Any,
    var selectedAnswer: Any,
    var correctAnswer: Any,
    val isCorrect: Boolean
)