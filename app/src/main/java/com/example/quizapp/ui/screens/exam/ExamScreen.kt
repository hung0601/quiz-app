package com.example.quizapp.ui.screens.exam

import android.util.Log
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
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizapp.constants.COUNTDOWN_TIME
import com.example.quizapp.constants.QuestionType
import com.example.quizapp.model.ExamResult
import com.example.quizapp.model.MultipleChoiceQuestion
import com.example.quizapp.model.Question
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.model.TrueFalseQuestion
import com.example.quizapp.model.TypeAnswerQuestion
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.ui.components.business.question.MultipleChoiceQuestion
import com.example.quizapp.ui.components.business.question.TrueFalseQuestion
import com.example.quizapp.ui.components.business.question.TypeAnswerQuestion
import com.example.quizapp.ui.components.business.result_dialog.QuestionResultDialog
import com.example.quizapp.ui.components.business.term.TermItem
import com.example.quizapp.ui.navigation.Screen
import com.example.quizapp.ui.screens.hooks.LoadingScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExamScreen(studySet: StudySetDetail, navController: NavController) {
    val examViewModel =
        hiltViewModel<ExamViewModel, ExamViewModel.ExamViewModelFactory> { factory ->
            factory.create(studySet)
        }
    val uiState by examViewModel.uiState.collectAsState()
    val color1 = Color(0xfff6f7fb)
    val step = remember { mutableIntStateOf(1) }
    var openResultDialog by remember { mutableStateOf(false) }
    val storeResultResponse by examViewModel.storeResultResponse.collectAsState()
    val studySetResponse by examViewModel.studySetResponse.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = studySetResponse is ResponseHandlerState.Success) {
        scope.launch {
            if (studySetResponse is ResponseHandlerState.Success) {
                examViewModel.randomQuestion()
                examViewModel.setLoading(false)
                step.intValue = 1
            }
        }
    }

    LaunchedEffect(key1 = storeResultResponse is ResponseHandlerState.Success) {
        scope.launch {
            if (storeResultResponse is ResponseHandlerState.Success) {
                examViewModel.setLoading(false)
                step.intValue = 2
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            IconButton(onClick = {
                navController.popBackStack(Screen.StudySet.route, true)
                navController.navigate(Screen.StudySet.passId(studySet.id))
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
                        text = (uiState.currentTerm + 1).toString() + "/" + uiState.questionList.size.toString(),
                        style = TextStyle(fontSize = 18.sp)
                    )
                    AnimatedContent(
                        targetState = uiState.currentTerm, transitionSpec = {
                            if (targetState > initialState) {
                                slideInHorizontally { width -> width } + fadeIn() with slideOutHorizontally { width -> -width }
                            } else {
                                slideInHorizontally { width -> -width } + fadeIn() with slideOutHorizontally { width -> width }
                            }.using(
                                SizeTransform(clip = false)
                            )
                        }, label = "", modifier = Modifier.weight(1F)
                    ) {
                        QuestionCard(
                            examQuestion = uiState.questionList[it],
                            handleNext = {
                                openResultDialog = true
                            },
                            handleAddQuestion = {
                                examViewModel.addResult(it)
                            })
                    }

                }
            }
        } else {
            ExamResultScreen(examUiState = uiState, examViewModel, onExit = {
                navController.popBackStack(Screen.StudySet.route, true)
                navController.navigate(Screen.StudySet.passId(studySet.id))
            })
        }
    }
    if (openResultDialog && uiState.currentQuestionResult != null) {
        QuestionResultDialog(
            onConfirmation = {
                openResultDialog = false
                if (uiState.currentTerm + 1 == uiState.questionList.size) {
                    examViewModel.setLoading(true);
                    examViewModel.sendResults()
                }
                examViewModel.setCurrentTerm(uiState.currentTerm + 1)
            },
            isCorrect = uiState.currentQuestionResult!!.isCorrect,
            correctAnswer = uiState.currentQuestionResult!!.correctAnswer.toString(),
            selectedAnswer = uiState.currentQuestionResult!!.selectedAnswer.toString(),
        )
    }

}

@Composable
fun ExamResultScreen(
    examUiState: ExamUiState, examViewModel: ExamViewModel, onExit: () -> Unit
) {
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
        LazyColumn(
            modifier = Modifier.weight(1F),
        ) {
            itemsIndexed(examUiState.examResults) { index, result ->
                val term =
                    examUiState.studySetDetail.terms.find { it.id == result.question.termReferentId }
                if (term != null) {
                    TermItem(
                        term = term,
                        modifier = if (result.isCorrect) Modifier else Modifier.background(
                            Color(0xFFFFCDD2)
                        )
                    )
                    if (index != examUiState.questionList.lastIndex) {
                        Divider(
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                            thickness = 1.dp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
        Divider(modifier = Modifier.padding(top = 10.dp, start = 5.dp, end = 5.dp, bottom = 10.dp))
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
                examViewModel.setLoading(true)
                examViewModel.fetchStudySet()
            }) {
                Text(text = "Continue")
            }
        }
    }
}


@Composable
fun QuestionCard(
    examQuestion: Question,
    handleNext: () -> Unit,
    handleCountDown: () -> Unit = {},
    handleAddQuestion: (ExamResult) -> Unit,
    hasTime: Boolean = false
) {
    val json = Json { ignoreUnknownKeys = true }
    var timeLeft by remember { mutableFloatStateOf(0F) }
    if (hasTime) {
        LaunchedEffect(key1 = timeLeft, block = {
            if (timeLeft < COUNTDOWN_TIME) {
                delay(100L)
                Log.d("count", timeLeft.toString())
                timeLeft += 0.1F
            } else {
                handleCountDown()
                handleNext()
            }
        })
    }
    Column {
        if (hasTime) {
            LinearProgressIndicator(
                progress = (timeLeft.toFloat() / 10),
                modifier = Modifier.fillMaxWidth()
            )
        }
        when (examQuestion.questionType) {
            QuestionType.MultipleChoiceQuestion.value -> {
                val questionDetail =
                    json.decodeFromString<MultipleChoiceQuestion>(examQuestion.question.toString())
                MultipleChoiceQuestion(
                    question = examQuestion,
                    questionDetail = questionDetail,
                    handleNext = handleNext,
                    handleAddQuestion = handleAddQuestion
                )
            }

            QuestionType.TypeAnswerQuestion.value -> {
                val questionDetail =
                    json.decodeFromString<TypeAnswerQuestion>(examQuestion.question.toString())
                TypeAnswerQuestion(
                    question = examQuestion,
                    questionDetail = questionDetail,
                    handleNext = handleNext,
                    handleAddQuestion = handleAddQuestion
                )
            }

            QuestionType.TrueFalseQuestion.value -> {
                val questionDetail =
                    json.decodeFromString<TrueFalseQuestion>(examQuestion.question.toString())
                TrueFalseQuestion(
                    question = examQuestion,
                    questionDetail = questionDetail,
                    handleNext = handleNext,
                    handleAddQuestion = handleAddQuestion
                )
            }
        }
    }
}

fun handleCountDown(examQuestion: Question): ExamResult? {
    val json = Json { ignoreUnknownKeys = true }
    when (examQuestion.questionType) {
        QuestionType.MultipleChoiceQuestion.value -> {
            val questionDetail =
                json.decodeFromString<MultipleChoiceQuestion>(examQuestion.question.toString())
            return ExamResult(
                examQuestion,
                questionDetail,
                null,
                questionDetail.answers[questionDetail.correctAnswer],
                false
            )
        }

        QuestionType.TypeAnswerQuestion.value -> {
            val questionDetail =
                json.decodeFromString<TypeAnswerQuestion>(examQuestion.question.toString())
            return ExamResult(
                examQuestion,
                questionDetail,
                null,
                questionDetail.correctAnswer,
                false
            )
        }

        QuestionType.TrueFalseQuestion.value -> {
            val questionDetail =
                json.decodeFromString<TrueFalseQuestion>(examQuestion.question.toString())
            return ExamResult(
                examQuestion,
                questionDetail,
                null,
                questionDetail.correctAnswer,
                false
            )
        }
    }
    return null
}
