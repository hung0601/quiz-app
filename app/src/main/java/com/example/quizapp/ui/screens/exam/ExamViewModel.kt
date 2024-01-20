package com.example.quizapp.ui.screens.exam

import androidx.lifecycle.ViewModel
import com.example.quizapp.network.QuizApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(private val quizApiRepository: QuizApiRepository) :
    ViewModel() {

    private var _uiState = MutableStateFlow(ExamUiState())
    val uiState: StateFlow<ExamUiState> = _uiState.asStateFlow()

    fun setCurrentTerm(currentTerm: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentTerm = currentTerm,
            )
        }
    }

    fun addQuestion(examResult: ExamResult) {
        _uiState.update { currentState ->
            currentState.copy(
                examResults = currentState.examResults.plus(examResult)
            )
        }
    }


}