package com.example.quizapp.ui.screens.game

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizapp.R
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.ui.components.basic.button.CustomButton
import com.example.quizapp.ui.components.basic.card.CustomCard
import com.example.quizapp.ui.navigation.Screen
import kotlinx.coroutines.delay

@SuppressLint("DefaultLocale")
@Composable
fun MatchGameScreen(
    navController: NavController,
    studySet: StudySetDetail
) {
    val matchGameModel =
        hiltViewModel<MatchGameModel, MatchGameModel.MatchGameModelFactory> { factory ->
            factory.create(studySet)
        }
    val uiState by matchGameModel.uiState.collectAsState()
    var timeLeft by remember { mutableFloatStateOf(0F) }

    LaunchedEffect(key1 = timeLeft, block = {
        if (uiState.step == 1) {
            delay(100L)
            timeLeft += 0.1F
        } else {

        }
    })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimary),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(0.5f)
            ) {
                IconButton(onClick = {
                    navController.popBackStack(Screen.StudySet.route, true)
                    navController.navigate(Screen.StudySet.passId(studySet.id))
                }) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                }
                Text(
                    text = String.format("%.1f", timeLeft) + "s",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "Match Game",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { /*TODO*/ }, modifier = Modifier.weight(0.5f)) {
                Icon(imageVector = Icons.Outlined.VolumeUp, contentDescription = null)
            }

        }
        if (uiState.step == 1) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                CardsGrid(cards = uiState.cards, matchGameModel = matchGameModel)
            }
        } else {
            EndGameScreen(
                navController = navController,
                gameModel = matchGameModel,
                result = timeLeft,
                onTryAgain = { timeLeft = 0F }
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun EndGameScreen(
    navController: NavController,
    gameModel: MatchGameModel,
    result: Float,
    onTryAgain: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.complete),
            contentDescription = null,
            modifier = Modifier.height(80.dp)
        )
        Text(
            text = "You have completed the game.",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Text(
            text = "${String.format("%.1f", result)}s",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 10.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 10.dp)
        ) {
            CustomButton(onClick = {
                navController.navigateUp()
            }) {
                Text(text = "Exit", color = Color.White)
            }
            CustomButton(onClick = {
                onTryAgain()
                gameModel.reset()
            }) {
                Text(text = "Try again", color = Color.White)
            }
        }
    }
}

@Composable
private fun CardsGrid(cards: List<GameCardItem> = listOf(), matchGameModel: MatchGameModel) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth(),
    ) {
        itemsIndexed(cards) { index, card ->
            CardItem(
                card = card,
                onClick = {
                    matchGameModel.selectCard(index)
                },
                modifier = Modifier
                    .height(150.dp)
                    .padding(3.dp)
            )
        }
    }
}

@Composable
private fun CardItem(
    card: GameCardItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
    ) {
        if (!card.isMatched) {
            CustomCard(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    onClick()
                }
            ) {
                Row(
                    modifier
                        .fillMaxSize()
                        .background(
                            color = if (card.isSelected) MaterialTheme.colorScheme.primary else Color.White
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (card.type == MatchCardType.TERM)
                        Text(text = card.term.term)
                    else
                        Text(text = card.term.definition)
                }
            }
        }
    }
}