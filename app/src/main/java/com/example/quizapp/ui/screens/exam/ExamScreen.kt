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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.model.Term
import com.example.quizapp.ui.components.card.CustomCard
import kotlin.random.Random

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExamScreen(studySet: StudySetDetail) {
    val examViewModel = hiltViewModel<ExamViewModel>()
    val uiState by examViewModel.uiState.collectAsState()
    val color1 = Color(0xfff6f7fb)
    val step = remember { mutableIntStateOf(1) }
    if (step.intValue == 1) {
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
                    text = (uiState.currentTerm + 1).toString() + "/" + studySet.terms.size.toString(),
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
                    it
                    QuestionCard(
                        currentQuestion = it,
                        studySet = studySet,
                        handleFinish = { step.intValue = 2 },
                        examViewModel
                    )
                }

            }
        }
    } else {
        ExamResultScreen(examUiState = uiState)
    }
}

@Composable
fun ExamResultScreen(examUiState: ExamUiState) {
    var correctAnswers = 0;
    examUiState.examResults.forEach {
        if (it.answers[it.selectedAnswer] == it.term) correctAnswers += 1
    }
    val point = (correctAnswers / examUiState.examResults.size) * 10
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

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(examUiState.examResults) { index, it ->
                val isCorrect = it.answers[it.selectedAnswer] == it.term
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
                        if (isCorrect) Icon(
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
                            text = "Question ${index + 1}: ${it.question}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Column(modifier = Modifier.padding(start = 30.dp)) {
                        Text(
                            text = "Selected answer: ${it.answers[it.selectedAnswer].definition}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Correct answer: ${it.term.definition}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionCard(
    currentQuestion: Int,
    studySet: StudySetDetail,
    handleFinish: () -> Unit,
    examViewModel: ExamViewModel
) {
    val currentTerm = studySet.terms[currentQuestion]
    val answers = randomAnswer(currentQuestion, studySet)
    val question = "Choose the correct answer\nDefinition of ${currentTerm.term} ?";
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = question, style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 50.dp, top = 10.dp)
        )
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
                    examViewModel.addQuestion(ExamResult(currentTerm, question, answers, 0))
                    if (currentQuestion + 1 == studySet.terms.size) handleFinish()
                    else examViewModel.setCurrentTerm(currentQuestion + 1)
                }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = answers[0].definition,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }
            CustomCard(modifier = Modifier
                .weight(1f)
                .height(150.dp),
                onClick = {
                    examViewModel.addQuestion(ExamResult(currentTerm, question, answers, 1))
                    if (currentQuestion + 1 == studySet.terms.size) handleFinish()
                    else examViewModel.setCurrentTerm(currentQuestion + 1)
                }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = answers[1].definition,
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
                    examViewModel.addQuestion(ExamResult(currentTerm, question, answers, 2))
                    if (currentQuestion + 1 == studySet.terms.size) handleFinish()
                    else examViewModel.setCurrentTerm(currentQuestion + 1)
                }) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = answers[2].definition,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

            }
            CustomCard(
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp),
                onClick = {
                    examViewModel.addQuestion(ExamResult(currentTerm, question, answers, 3))
                    if (currentQuestion + 1 == studySet.terms.size) handleFinish()
                    else examViewModel.setCurrentTerm(currentQuestion + 1)
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = answers[3].definition,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}


fun randomAnswer(currentQuestion: Int, studySet: StudySetDetail): List<Term> {
    val currentTerm = studySet.terms[currentQuestion]
    val answers = mutableListOf<Term>();
    val correctIndex = Random.nextInt(1, 4)
    var index = Random.nextInt(0, studySet.terms.size - 1)
    for (i in 1..4) {
        if (correctIndex == i) {
            answers.add(
                currentTerm
            )
        } else {
            while (answers.contains(studySet.terms[index]) || index == currentQuestion) {
                index = Random.nextInt(0, studySet.terms.size - 1)
            }
            answers.add(studySet.terms[index])
        }
    }
    return answers
}