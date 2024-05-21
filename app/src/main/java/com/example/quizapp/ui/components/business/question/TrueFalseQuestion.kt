package com.example.quizapp.ui.components.business.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.model.ExamResult
import com.example.quizapp.model.Question
import com.example.quizapp.model.TrueFalseQuestion
import com.example.quizapp.ui.components.basic.card.CustomCard

@Composable
fun TrueFalseQuestion(
    question: Question,
    questionDetail: TrueFalseQuestion,
    handleNext: () -> Unit,
    handleAddQuestion: (ExamResult) -> Unit,
) {
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
            CustomCard(modifier = Modifier
                .weight(1f)
                .height(150.dp),
                onClick = {
                    handleSelectAnswer(
                        question,
                        questionDetail,
                        handleNext,
                        handleAddQuestion,
                        true
                    )
                }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "True",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }
            CustomCard(modifier = Modifier
                .weight(1f)
                .height(150.dp),
                onClick = {
                    handleSelectAnswer(
                        question,
                        questionDetail,
                        handleNext,
                        handleAddQuestion,
                        false
                    )
                }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "False",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }
        }
    }
}

fun handleSelectAnswer(
    question: Question,
    questionDetail: TrueFalseQuestion,
    handleNext: () -> Unit,
    handleAddQuestion: (ExamResult) -> Unit,
    selectedAnswer: Boolean,
) {
    val result = ExamResult(
        question,
        questionDetail,
        selectedAnswer,
        questionDetail.correctAnswer,
        questionDetail.correctAnswer == selectedAnswer
    )
    handleAddQuestion(result)
    handleNext()
}