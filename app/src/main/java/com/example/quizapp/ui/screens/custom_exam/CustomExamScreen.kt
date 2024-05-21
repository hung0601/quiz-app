package com.example.quizapp.ui.screens.custom_exam

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizapp.model.ExamDetail
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.business.question_result.QuestionResult
import com.example.quizapp.ui.navigation.Screen
import com.example.quizapp.ui.screens.exam.QuestionCard
import com.example.quizapp.ui.screens.exam.handleCountDown
import com.example.quizapp.ui.screens.hooks.ErrorScreen
import com.example.quizapp.ui.screens.hooks.LoadingScreen
import com.example.quizapp.ui.view_model.TextToSpeechModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomExamScreen(navController: NavController) {
    val examViewModel = hiltViewModel<CustomExamViewModel>()
    val uiState by examViewModel.uiState.collectAsState()
    val examResponse by examViewModel.examResponse.collectAsState()
    val color1 = Color(0xfff6f7fb)
    val step = remember { mutableIntStateOf(1) }

    when (examResponse) {
        is ResponseHandlerState.Success -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onPrimary)
                ) {
                    IconButton(onClick = {
                        navController.popBackStack(Screen.StudySet.route, true)
                        navController.navigate(
                            Screen.StudySet.passId(
                                (examResponse as ResponseHandlerState.Success<ExamDetail>).data.studySetId
                            )
                        )
                    }) {
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                    }
                }
                if (uiState.isLoading) {
                    LoadingScreen()
                } else if (step.intValue == 1) {
                    Row(
                        modifier = Modifier
                            .background(color = color1, shape = RectangleShape)
                            .fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = (uiState.currentQuestion + 1).toString() + "/" + uiState.questionList.size.toString(),
                                style = TextStyle(fontSize = 18.sp)
                            )
                            AnimatedContent(
                                targetState = uiState.currentQuestion,
                                transitionSpec = {
                                    if (targetState > initialState) {
                                        slideInHorizontally { width -> width } + fadeIn() with
                                                slideOutHorizontally { width -> -width }
                                    } else {
                                        slideInHorizontally { width -> -width } + fadeIn() with
                                                slideOutHorizontally { width -> width }
                                    }.using(
                                        SizeTransform(clip = false)
                                    )
                                }, label = "",
                                modifier = Modifier.weight(1F)
                            ) {
                                QuestionCard(
                                    hasTime = true,
                                    examQuestion = uiState.questionList[it],
                                    handleNext = {
                                        if (uiState.currentQuestion + 1 == uiState.questionList.size) {
                                            examViewModel.submitExam()
                                            step.intValue = 2
                                        }
                                        examViewModel.setCurrentQuestion(uiState.currentQuestion + 1)
                                    },
                                    handleAddQuestion = {
                                        examViewModel.addResult(it)
                                    },
                                    handleCountDown = {
                                        val result =
                                            handleCountDown(examQuestion = uiState.questionList[it])
                                        if (result != null) examViewModel.addResult(result)
                                    })
                            }
                        }
                    }
                } else {
                    CustomExamResultScreen(
                        examUiState = uiState,
                        examViewModel,
                        onContinue = {
                            step.intValue = 1
                        },
                        onExit = {
                            navController.popBackStack(Screen.StudySet.route, true)
                            navController.navigate(
                                Screen.StudySet.passId(
                                    (examResponse as ResponseHandlerState.Success<ExamDetail>).data.studySetId
                                )
                            )
                        })
                }
            }
        }

        is ResponseHandlerState.Loading -> {
            LoadingScreen()
        }

        else -> {
            ErrorScreen()
        }

    }

}

@Composable
fun CustomExamResultScreen(
    examUiState: CustomExamUiState,
    examViewModel: CustomExamViewModel,
    onContinue: () -> Unit,
    onExit: () -> Unit
) {
    val textToSpeechModel = TextToSpeechModel()
    var correctAnswers = 0
    examUiState.examResults.forEach {
        if (it.isCorrect) correctAnswers += 1
    }
    val point = (correctAnswers.toFloat() / examUiState.examResults.size) * 10
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(text = "Point: ${point.toInt()}", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "Correct answers: ${correctAnswers}/${examUiState.examResults.size}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        Divider(modifier = Modifier.padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 10.dp))
        LazyColumn(modifier = Modifier.weight(1F)) {
            itemsIndexed(examUiState.examResults) { index, result ->
                QuestionResult(
                    index = index,
                    result = result,
                    textToSpeechModel = textToSpeechModel
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(modifier = Modifier.padding(end = 8.dp), onClick = { onExit() }) {
                Text(text = "Exit")
            }
            Button(modifier = Modifier.padding(start = 8.dp), onClick = {
                examViewModel.reset()
                onContinue()
            }) {
                Text(text = "Try again")
            }
        }
    }
}