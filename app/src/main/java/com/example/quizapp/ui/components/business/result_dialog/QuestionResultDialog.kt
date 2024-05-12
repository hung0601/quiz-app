package com.example.quizapp.ui.components.business.result_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.quizapp.ui.theme.md_theme_error
import com.example.quizapp.ui.theme.md_theme_success
import kotlinx.coroutines.delay

@Composable
fun QuestionResultDialog(
    onConfirmation: () -> Unit,
    isCorrect: Boolean,
    correctAnswer: String,
    selectedAnswer: String
) {
    var timeLeft by remember { mutableStateOf(1) }
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0x4c949494)),
            contentAlignment = Alignment.Center
        ) {
            ElevatedCard(
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight(),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (isCorrect) {
                        LaunchedEffect(key1 = timeLeft) {
                            while (timeLeft > 0) {
                                delay(1000L)
                                timeLeft--
                                if (timeLeft == 0) onConfirmation()
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(md_theme_success)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "\uD83E\uDD73 Correct",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                        }
                        Row(modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)) {
                            Text(
                                text = selectedAnswer,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(md_theme_error)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "\uD83D\uDE25 Wrong",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                        }
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "Correct answer:",
                                style = MaterialTheme.typography.titleMedium,
                                color = md_theme_success
                            )
                            Text(
                                text = correctAnswer,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "You select:",
                                style = MaterialTheme.typography.titleMedium,
                                color = md_theme_error
                            )
                            Text(
                                text = selectedAnswer,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Button(
                                onClick = { onConfirmation() },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                            ) {
                                Text("Continue")
                            }
                        }
                    }
                }
            }
        }
    }
}