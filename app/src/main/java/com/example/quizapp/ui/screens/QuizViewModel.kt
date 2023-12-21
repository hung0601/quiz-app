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
package com.example.quizapp.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.quizapp.QuizApplication
import com.example.quizapp.data.QuizApiRepository
import com.example.quizapp.model.StudySet
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 * UI state for the Home screen
 */
sealed interface QuizUiState {
    data class Success(val studySets: List<StudySet>) : QuizUiState
    object Error : QuizUiState
    object Loading : QuizUiState
}

class MarsViewModel(private val quizApiRepository: QuizApiRepository) : ViewModel() {
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
            quizUiState = try {
                QuizUiState.Success(quizApiRepository.getStudySets())
            } catch (e: IOException) {
                QuizUiState.Error
            } catch (e: HttpException) {
                QuizUiState.Error
            }
        }
    }

    /**
     * Factory for [MarsViewModel] that takes [QuizApiRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as QuizApplication)
                val quizappRepository = application.container.quizApiRepository
                MarsViewModel(quizApiRepository = quizappRepository)
            }
        }
    }
}
