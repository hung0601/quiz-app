/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.quizapp.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.StudySet
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.response_model.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Home screen
 */
sealed interface QuizUiState {
    data class Success(val studySets: List<StudySet>) : QuizUiState
    object Error : QuizUiState
    object Loading : QuizUiState
}

@HiltViewModel
class QuizViewModel @Inject constructor(private val quizApiRepository: QuizApiRepository) :
    ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var quizUiState: QuizUiState by mutableStateOf(QuizUiState.Loading)
        private set

    /**
     * Call getquizapp() on init so we can display status immediately.
     */
    init {
        getStudySets()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [StudySet] [List] [MutableList].
     */
    fun getStudySets() {
        viewModelScope.launch {
            quizUiState = QuizUiState.Loading
            val response = quizApiRepository.getStudySets()
            quizUiState = when (response) {
                is ApiResponse.Success -> {
                    QuizUiState.Success(response.data)
                }

                is ApiResponse.Error -> {
                    QuizUiState.Error
                }

                else -> {
                    QuizUiState.Error
                }
            }


        }
    }
}
