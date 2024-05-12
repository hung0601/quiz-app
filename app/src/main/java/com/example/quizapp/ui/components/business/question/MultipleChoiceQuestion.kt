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
import com.example.quizapp.model.MultipleChoiceQuestion
import com.example.quizapp.model.Question
import com.example.quizapp.ui.components.basic.card.CustomCard
import com.example.quizapp.ui.screens.exam.ExamResult
import com.example.quizapp.ui.screens.exam.ExamViewModel

@Composable
fun MultipleChoiceQuestion(
    question: Question,
    questionDetail: MultipleChoiceQuestion,
    handleNext: () -> Unit,
    examViewModel: ExamViewModel
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
                        examViewModel,
                        0
                    )
                }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = questionDetail.answers[0],
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
                        examViewModel,
                        1
                    )
                }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = questionDetail.answers[1],
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
                        examViewModel,
                        2
                    )
                }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = questionDetail.answers[2],
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
                        examViewModel,
                        3
                    )
                }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = questionDetail.answers[3],
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }
        }
    }
}

fun handleSelectAnswer(
    question: Question,
    questionDetail: MultipleChoiceQuestion,
    handleNext: () -> Unit,
    examViewModel: ExamViewModel,
    selectedAnswer: Int,
) {
    val result = ExamResult(
        question,
        questionDetail,
        questionDetail.answers[selectedAnswer],
        questionDetail.answers[questionDetail.correctAnswer],
        questionDetail.correctAnswer == selectedAnswer
    )
    examViewModel.addResult(
        result
    )
    handleNext()
}