package com.example.quizapp.ui.screens.game

import androidx.lifecycle.ViewModel
import com.example.quizapp.model.StudySetDetail
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel(assistedFactory = MatchGameModel.MatchGameModelFactory::class)
class MatchGameModel @AssistedInject constructor(
    @Assisted private val studySet: StudySetDetail
) : ViewModel() {
    private var _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    @AssistedFactory
    interface MatchGameModelFactory {
        fun create(studySet: StudySetDetail): MatchGameModel
    }

    init {
        createGameData()
    }

    private fun createGameData() {
        val selectedTerms = studySet.terms.shuffled().take(6)
        val termCards = selectedTerms.map { GameCardItem(term = it) }
        val definitionCards =
            selectedTerms.map { GameCardItem(term = it, type = MatchCardType.DEFINITION) }
        _uiState.value = _uiState.value.copy(cards = (termCards + definitionCards).shuffled())
    }

    fun selectCard(index: Int) {
        val card = _uiState.value.cards[index]
        val selected = _uiState.value.cards.filter {
            it != card && it.isSelected
        }
        if (selected.isNotEmpty() && selected[0].term == card.term) {
            _uiState.value = _uiState.value.copy(
                cards = _uiState.value.cards.map {
                    var result = it
                    if (it.term == card.term) result = it.copy(isSelected = false, isMatched = true)
                    result
                }
            )
        } else if (selected.isNotEmpty() && selected[0].term != card.term) {
            _uiState.value = _uiState.value.copy(
                cards = _uiState.value.cards.map {
                    var result = it
                    if (it.term == card.term || it.term == selected[0].term) result =
                        it.copy(isSelected = false)
                    result
                }
            )
        } else {
            _uiState.value = _uiState.value.copy(
                cards = _uiState.value.cards.map {
                    var result = it
                    if (it == card) result =
                        it.copy(isSelected = true)
                    result
                }
            )
        }
    }
}