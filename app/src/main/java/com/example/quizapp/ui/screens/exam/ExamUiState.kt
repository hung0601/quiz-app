package com.example.quizapp.ui.screens.exam

import com.example.quizapp.model.Term

data class ExamUiState(
    val currentTerm: Int = 0,
    var examResults: List<ExamResult> = mutableListOf()
)

data class ExamResult(
    val term: Term,
    val question: String,
    val answers: List<Term>,
    var selectedAnswer: Int,
)