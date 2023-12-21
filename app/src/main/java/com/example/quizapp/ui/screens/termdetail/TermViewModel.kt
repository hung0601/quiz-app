package com.example.quizapp.ui.screens.termdetail

import androidx.lifecycle.ViewModel
import com.example.quizapp.data.TermUiState
import com.example.quizapp.model.StudySet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TermViewModel : ViewModel() {

    /**
     * Cupcake state for this order
     */
    private val _uiState = MutableStateFlow(TermUiState())
    val uiState: StateFlow<TermUiState> = _uiState.asStateFlow()

    fun setCurrentSet(currentSet: StudySet){
        _uiState.update { currentState ->
            currentState.copy(
                currentSet = currentSet
            )
        }
    }
    /**
     * Set the quantity [numberCupcakes] of cupcakes for this order's state and update the price
     */

}