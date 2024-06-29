package com.example.quizapp.constants

enum class QuestionType(val value: String) {
    MultipleChoiceQuestion("App\\Models\\QuizQuestion"),
    TrueFalseQuestion("App\\Models\\TrueFalseQuestion"),
    TypeAnswerQuestion("App\\Models\\TypeAnswerQuestion")
}