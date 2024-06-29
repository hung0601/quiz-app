package com.example.quizapp.ui.screens.game

import com.example.quizapp.model.Term

data class GameUiState(
    val cards: List<GameCardItem> = emptyList(),
    val step: Int = 1,
    val time: Float = 0f
)

data class GameCardItem(
    val term: Term,
    val type: MatchCardType = MatchCardType.TERM,
    var isMatched: Boolean = false,
    var isSelected: Boolean = false,
)

enum class MatchCardType {
    TERM, DEFINITION
}