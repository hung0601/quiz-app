package com.example.quizapp.ui.screens.flash_card


import androidx.lifecycle.ViewModel
import com.example.quizapp.network.QuizApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class FlashCardModel @Inject constructor(private val quizApiRepository: QuizApiRepository) :
    ViewModel() {

    private var _uiState = MutableStateFlow(FlashCardUiState())
    val uiState: StateFlow<FlashCardUiState> = _uiState.asStateFlow()

    fun setCurrentTerm(currentTerm: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentTerm = currentTerm,
                isOpen = false
            )
        }
    }

    fun toggleOpen() {
        _uiState.update { currentState ->
            currentState.copy(
                isOpen = !currentState.isOpen
            )
        }
    }

}