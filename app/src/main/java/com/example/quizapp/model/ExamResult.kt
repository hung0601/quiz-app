package com.example.quizapp.model

data class ExamResult(
    val question: Question,
    val questionDetail: Any,
    var selectedAnswer: Any? = null,
    var correctAnswer: Any,
    val isCorrect: Boolean
)