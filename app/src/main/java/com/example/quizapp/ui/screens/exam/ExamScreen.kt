package com.example.quizapp.ui.screens.exam

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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.quizapp.constant.QuestionType
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
import com.example.quizapp.ui.navigation.Screen
import com.example.quizapp.ui.screens.hooks.LoadingScreen
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
    val scope = rememberCoroutineScope()

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
            IconButton(onClick = { navController.navigate(Screen.StudySet.passId(studySet.id)) }) {
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
                        targetState = uiState.currentTerm,
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
                            examQuestion = uiState.questionList[it],
                            handleNext = {
                                openResultDialog = true
                            },
                            examViewModel = examViewModel
                        )
                    }

                }
            }
        } else {
            ExamResultScreen(examUiState = uiState)
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
fun ExamResultScreen(examUiState: ExamUiState) {
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Continue")
            }
        }

//        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(10.dp),
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            itemsIndexed(examUiState.examResults) { index, it ->
//                when (it.question.questionType) {
//                    QuestionType.MultipleChoiceQuestion.value -> {
//                        MultipleChoiceQuestionResult(
//                            index,
//                            it,
//                            it.questionDetail as MultipleChoiceQuestion
//                        )
//                    }
//                }
//            }
//        }
    }
}

@Composable
fun QuestionCard(
    examQuestion: Question,
    handleNext: () -> Unit,
    examViewModel: ExamViewModel
) {
    val questionDetail: Any?
    when (examQuestion.questionType) {
        QuestionType.MultipleChoiceQuestion.value -> {
            questionDetail =
                Json.decodeFromString<MultipleChoiceQuestion>(examQuestion.question.toString())
            MultipleChoiceQuestion(
                question = examQuestion,
                questionDetail = questionDetail,
                handleNext = handleNext,
                examViewModel = examViewModel
            )
        }

        QuestionType.TypeAnswerQuestion.value -> {
            questionDetail =
                Json.decodeFromString<TypeAnswerQuestion>(examQuestion.question.toString())
            TypeAnswerQuestion(
                question = examQuestion,
                questionDetail = questionDetail,
                handleNext = handleNext,
                examViewModel = examViewModel
            )
        }

        QuestionType.TrueFalseQuestion.value -> {
            questionDetail =
                Json.decodeFromString<TrueFalseQuestion>(examQuestion.question.toString())
            TrueFalseQuestion(
                question = examQuestion,
                questionDetail = questionDetail,
                handleNext = handleNext,
                examViewModel = examViewModel
            )
        }
    }
}
