package com.example.quizapp.ui.components.business.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.model.ExamResult
import com.example.quizapp.model.Question
import com.example.quizapp.model.TypeAnswerQuestion
import com.example.quizapp.ui.components.basic.textfield.CustomTextField

@Composable
fun TypeAnswerQuestion(
    question: Question,
    questionDetail: TypeAnswerQuestion,
    handleNext: () -> Unit,
    handleAddQuestion: (ExamResult) -> Unit,
) {
    val inputValue = remember {
        mutableStateOf("")
    }
    val isInput = remember {
        mutableStateOf(false)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        if (question.hasAudio) {
            AudioQuestion(speechText = question.audioText ?: "")
        } else {
            Text(
                text = questionDetail.question, style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 50.dp, top = 10.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CustomTextField(
                value = inputValue.value,
                onValueChange = {
                    inputValue.value = it
                    isInput.value = true
                },
                isError = isInput.value && inputValue.value.isEmpty()
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                if (inputValue.value.isNotEmpty()) {
                    handleSelectAnswer(
                        question,
                        questionDetail,
                        handleNext,
                        handleAddQuestion,
                        inputValue.value
                    )
                }
            }) {
                Text(text = "Submit")
            }
        }
    }
}

fun handleSelectAnswer(
    question: Question,
    questionDetail: TypeAnswerQuestion,
    handleNext: () -> Unit,
    handleAddQuestion: (ExamResult) -> Unit,
    selectedAnswer: String,
) {
    val isCorrect = if (questionDetail.correctAnswer.compareTo(
            selectedAnswer,
            ignoreCase = true
        ) == 0
    ) true else false
    val result = ExamResult(
        question,
        questionDetail,
        selectedAnswer,
        questionDetail.correctAnswer,
        isCorrect
    )
    handleAddQuestion(result)
    handleNext()
}