package com.example.quizapp.ui.screens.quizstudy


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.quizapp.data.StudyUiState
import com.example.quizapp.model.StudySet
import com.example.quizapp.model.Term

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun QuizStudyScreen(studyViewModel: QuizStudyModel = viewModel(), studySet: StudySet) {
    val uiState by studyViewModel.uiState.collectAsState()
    val color1 = Color(0xfff6f7fb)
    Row(
        modifier = Modifier
            .background(color = color1, shape = RectangleShape)
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
                .wrapContentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = (uiState.currentTerm + 1).toString() + "/" + studySet.terms.size.toString(),

                )
            AnimatedContent(
                targetState = uiState.currentTerm,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { width -> width } + fadeIn() with
                                slideOutHorizontally{ width -> -width }
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() with
                                slideOutHorizontally { width -> width }
                    }.using(
                        SizeTransform(clip = false)
                    )
                }, label = ""
            ) {
                FlipCard(term = studySet.terms[uiState.currentTerm],
                    studyViewModel = studyViewModel,
                    uiState = uiState,
                    onCardClick = {})
            }


            Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Button(onClick = {
                    studyViewModel.setCurrentTerm(
                        getPrevTerm(
                            studySet.terms,
                            uiState.currentTerm
                        )
                    )
                }) {
                    Text(text = "Prev")
                }
                Button(onClick = {
                    studyViewModel.setCurrentTerm(
                        getNextTerm(
                            studySet.terms,
                            uiState.currentTerm
                        )
                    )
                }) {
                    Text(text = "Next")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlipCard(
    term: Term,
    studyViewModel: QuizStudyModel,
    uiState: StudyUiState,
    onCardClick: () -> Unit
) {

    var rotated by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(500), label = ""
    )

    val animateFront by animateFloatAsState(
        targetValue = if (!rotated) 1f else 0f,
        animationSpec = tween(500), label = ""
    )

    val animateBack by animateFloatAsState(
        targetValue = if (rotated) 1f else 0f,
        animationSpec = tween(500), label = ""
    )


    ElevatedCard(
        onClick = {
            studyViewModel.toggleOpen()
            onCardClick()
            rotated = !rotated
        },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffffffff),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp)
            .padding(5.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }

    )
    {
        Column(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = if (rotated) animateBack else animateFront
                    rotationY = rotation
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!uiState.isOpen) {
                Text(
                    text = term.term,
                    style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.SemiBold),
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    AsyncImage(
                        model = term.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(start = 50.dp, end = 50.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop,
                    )
                    Text(
                        text = term.definition,
                        style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Normal)
                    )
                }
            }

        }

    }

}

fun getNextTerm(terms: List<Term>, currentIndex: Int): Int {
    return if (currentIndex + 1 < terms.size) currentIndex + 1
    else 0
}

fun getPrevTerm(terms: List<Term>, currentIndex: Int): Int {
    return if (currentIndex - 1 >= 0) currentIndex - 1
    else terms.size - 1
}