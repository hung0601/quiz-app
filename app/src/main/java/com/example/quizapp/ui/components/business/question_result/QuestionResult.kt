package com.example.quizapp.ui.components.business.question_result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quizapp.constant.QuestionType
import com.example.quizapp.model.ExamResult
import com.example.quizapp.model.MultipleChoiceQuestion
import com.example.quizapp.model.TrueFalseQuestion
import com.example.quizapp.ui.components.business.question.AudioQuestion
import com.example.quizapp.ui.view_model.TextToSpeechModel
import kotlinx.serialization.json.Json

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QuestionResult(
    index: Int,
    result: ExamResult,
    textToSpeechModel: TextToSpeechModel
) {
    val json = Json { ignoreUnknownKeys = true }
    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (result.isCorrect) {
                Icon(
                    imageVector = Icons.Outlined.Circle,
                    tint = Color.Green,
                    contentDescription = null
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    tint = Color.Red,
                    contentDescription = null
                )
            }
            Text(
                text = "${index + 1}. " + result.question.question.getValue("question")
                    .toString()
                    .removeSurrounding("\"")
            )
        }
        if (result.question.hasAudio) {
            AudioQuestion(
                speechText = result.question.audioText ?: "",
                textToSpeechModel = textToSpeechModel
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            when (result.question.questionType) {
                QuestionType.MultipleChoiceQuestion.value -> {
                    val questionDetail =
                        json.decodeFromString<MultipleChoiceQuestion>(result.question.question.toString())
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        maxItemsInEachRow = 2,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        questionDetail.answers.mapIndexed { index, it ->
                            Row(
                                modifier = Modifier.weight(1F),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = index == questionDetail.correctAnswer,
                                    onClick = {},
                                    enabled = false
                                )
                                Text(text = it)
                            }
                        }
                    }
                }

                QuestionType.TrueFalseQuestion.value -> {
                    val questionDetail =
                        json.decodeFromString<TrueFalseQuestion>(result.question.question.toString())
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        maxItemsInEachRow = 2,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.weight(1F),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = questionDetail.correctAnswer,
                                onClick = {},
                                enabled = false
                            )
                            Text(text = "True")
                        }
                        Row(
                            modifier = Modifier.weight(1F),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = !questionDetail.correctAnswer,
                                onClick = {},
                                enabled = false
                            )
                            Text(text = "False")
                        }
                    }
                }

                QuestionType.TypeAnswerQuestion.value -> {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = result.selectedAnswer.toString(),
                        onValueChange = {},
                        enabled = false
                    )
                }
            }
        }
        Divider(modifier = Modifier.padding(top = 10.dp))
    }
}