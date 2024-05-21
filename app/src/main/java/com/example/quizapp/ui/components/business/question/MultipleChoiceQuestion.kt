package com.example.quizapp.ui.components.business.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.model.ExamResult
import com.example.quizapp.model.MultipleChoiceQuestion
import com.example.quizapp.model.Question
import com.example.quizapp.ui.components.basic.card.CustomCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MultipleChoiceQuestion(
    question: Question,
    questionDetail: MultipleChoiceQuestion,
    handleNext: () -> Unit,
    handleAddQuestion: (ExamResult) -> Unit
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
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            maxItemsInEachRow = 2
        ) {
            for (i in 0..3) {
                CustomCard(modifier = Modifier
                    .weight(1f)
                    .height(150.dp)
                    .padding(bottom = 10.dp),
                    onClick = {
                        handleSelectAnswer(
                            question,
                            questionDetail,
                            handleNext,
                            handleAddQuestion,
                            i
                        )
                    }) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = questionDetail.answers[i],
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                }
            }
        }
    }
}

fun handleSelectAnswer(
    question: Question,
    questionDetail: MultipleChoiceQuestion,
    handleNext: () -> Unit,
    handleAddQuestion: (ExamResult) -> Unit,
    selectedAnswer: Int,
) {
    val result = ExamResult(
        question,
        questionDetail,
        questionDetail.answers[selectedAnswer],
        questionDetail.answers[questionDetail.correctAnswer],
        questionDetail.correctAnswer == selectedAnswer
    )
    handleAddQuestion(result)
    handleNext()
}