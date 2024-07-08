package com.example.quizapp.ui.screens.flash_card


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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.model.Term
import com.example.quizapp.ui.components.basic.button.CustomButton
import com.example.quizapp.ui.theme.md_theme_error
import com.example.quizapp.ui.theme.md_theme_success
import kotlin.math.abs


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FlashCardScreen(studySet: StudySetDetail, navController: NavController) {
    val flashCardModel =
        hiltViewModel<FlashCardModel, FlashCardModel.FlashCardModelFactory> { factory ->
            factory.create(studySet)
        }
    val uiState by flashCardModel.uiState.collectAsState()
    val color1 = Color(0xfff6f7fb)
    Row(
        modifier = Modifier
            .background(color = color1, shape = RectangleShape)
            .fillMaxSize()
    ) {
        if (uiState.step == 0) {
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = (flashCardModel.findCurrentIndex(uiState.currentTerm) + 1).toString() + "/" + uiState.terms.size,
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
                )
                AnimatedContent(
                    targetState = uiState.currentTerm,
                    transitionSpec = {
                        if (flashCardModel.findIndex(targetState) > flashCardModel.findIndex(
                                initialState
                            )
                        ) {
                            slideInHorizontally { width -> width } + fadeIn() with
                                    slideOutHorizontally { width -> -width }
                        } else {
                            slideInHorizontally { width -> -width } + fadeIn() with
                                    slideOutHorizontally { width -> width }
                        }.using(
                            SizeTransform(clip = false)
                        )
                    }, label = "",
                    modifier = Modifier
                        .weight(1F)
                        .padding(10.dp)
                ) {
                    FlipCard(term = it,
                        lang = studySet.termLang,
                        flashCardModel = flashCardModel,
                        uiState = uiState,
                        onCardClick = {})
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {

                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = "Back button",
                        tint = md_theme_error,
                        modifier = Modifier
                            .size(60.dp)
                            .clickable {
                                flashCardModel.next()
                            }
                    )
                    Icon(
                        Icons.Outlined.Check,
                        contentDescription = "Next button",
                        tint = md_theme_success,
                        modifier = Modifier
                            .size(60.dp)
                            .clickable {
                                flashCardModel.remove()
                            }
                    )
                }
            }
        } else {
            FlashCardEndScreen(navController = navController, flashCardModel = flashCardModel)
        }
    }
}

@Composable
fun FlashCardEndScreen(
    navController: NavController,
    flashCardModel: FlashCardModel,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "You have gone through all the flashcards, do you want to start again?",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CustomButton(onClick = {
                navController.navigateUp()
            }) {
                Text(text = "Exit")
            }
            CustomButton(onClick = {
                flashCardModel.reset()
            }) {
                Text(text = "OK")
            }
        }
    }
}


@Composable
fun FlipCard(
    term: Term,
    lang: String,
    flashCardModel: FlashCardModel,
    uiState: FlashCardUiState,
    onCardClick: () -> Unit,
) {

    var rotated by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (rotated) 180f else 0f,
        animationSpec = tween(500), label = ""
    )
    val rotation180 by animateFloatAsState(
        targetValue = 180f,
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

    var offsetX by remember { mutableStateOf(0f) }
    var componentWidth by remember { mutableStateOf(0.dp) }
    val localDensity = LocalDensity.current
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffffffff),
        ),
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    flashCardModel.toggleOpen()
                    onCardClick()
                    rotated = !rotated
                }
            )
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }
            .onGloballyPositioned {
                componentWidth = with(localDensity) {
                    it.size.width.toDp()
                }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val (x, y) = dragAmount
                        offsetX += dragAmount.x

                    },
                    onDragEnd = {
                        when {
                            (offsetX < 0F && abs(offsetX) > componentWidth.toPx() / 4) -> {
                                flashCardModel.next()
                            }

                            (offsetX > 0 && abs(offsetX) > componentWidth.toPx() / 4) -> {
                                flashCardModel.prev()
                            }
                        }
                        offsetX = 0F
                    }
                )
            }

    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .graphicsLayer {
                    alpha = if (rotated) animateBack else animateFront
                    rotationY = rotation
                },
        ) {
            var isLike by rememberSaveable {
                mutableStateOf(false)
            }
            Box(contentAlignment = Alignment.TopEnd, modifier = Modifier.fillMaxWidth()) {
                Icon(
                    if (isLike) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = Color(0xffE35454),
                    modifier = Modifier
                        .clickable { isLike = !isLike }
                        .size(30.dp)
                )
            }
            if (!rotated) {
                Icon(
                    Icons.Outlined.VolumeUp,
                    contentDescription = null,
                    tint = Color(0xff586380),
                    modifier = Modifier
                        .clickable {
                            flashCardModel.textToSpeech(term.term, lang)
                        }
                        .size(35.dp)
                )
            }

        }
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
            if (!rotated) {
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
                        .graphicsLayer {
                            alpha = if (rotated) animateBack else animateFront
                            rotationY = rotation
                        }

                ) {
                    if (term.imageUrl != null) {
                        AsyncImage(
                            model = term.imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(start = 50.dp, end = 50.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .graphicsLayer {
                                    alpha = if (rotated) animateBack else animateFront
                                    rotationY = rotation180
                                },
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Text(
                        text = term.definition,
                        style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Normal),
                        modifier = Modifier.graphicsLayer {
                            alpha = if (rotated) animateBack else animateFront
                            rotationY = rotation180
                        }
                    )
                }
            }

        }

    }

}
