package com.example.quizapp.ui.components.business.question_result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quizapp.model.MultipleChoiceQuestion
import com.example.quizapp.ui.screens.exam.ExamResult

@Composable
fun MultipleChoiceQuestionResult(
    index: Int,
    result: ExamResult,
    questionDetail: MultipleChoiceQuestion
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .drawBehind {
            val y = size.height - 4 / 2
            drawLine(
                Color.LightGray,
                Offset(0f, y),
                Offset(size.width, y),
                4f
            )
        }
        .padding(bottom = 10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            if (result.isCorrect) Icon(
                imageVector = Icons.Outlined.Circle,
                contentDescription = null,
                tint = Color.Green
            )
            else Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
                tint = Color.Red
            )
            Text(
                text = "Question ${index + 1}: ${questionDetail.question}",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Column(modifier = Modifier.padding(start = 30.dp)) {
            Text(
                text = "Selected answer: ${result.selectedAnswer}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Correct answer: ${result.correctAnswer}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

