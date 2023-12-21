package com.example.quizapp.ui.screens.quizstudy

import androidx.lifecycle.ViewModel
import com.example.quizapp.data.StudyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuizStudyModel(): ViewModel(){
    private val _uiState = MutableStateFlow(StudyUiState())
    val uiState: StateFlow<StudyUiState> = _uiState.asStateFlow()

    fun setCurrentTerm(currentTerm: Int){
        _uiState.update { currentState ->
            currentState.copy(
                currentTerm = currentTerm,
                isOpen = false
            )
        }
    }

    fun toggleOpen(){
        _uiState.update { currentState ->
            currentState.copy(
                isOpen = !currentState.isOpen
            )
        }
    }

}