package com.example.quizapp.ui.screens.set_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.session.SessionCache
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.response_model.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface StudySetUiState {
    data class Success(val studySet: StudySetDetail) : StudySetUiState
    object Error : StudySetUiState
    object Loading : StudySetUiState
}

@HiltViewModel
class SetDetailModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
    private val savedStateHandle: SavedStateHandle,
    private val sessionCache: SessionCache,
) :
    ViewModel() {
    var uiState: StudySetUiState by mutableStateOf(StudySetUiState.Loading)
        private set
    private val itemId: Int = checkNotNull(savedStateHandle.get<Int>("id"))

    val currentUser = sessionCache.getActiveSession()?.user

    init {
        getStudySet()
    }


    fun getStudySet() {
        viewModelScope.launch {
            uiState = StudySetUiState.Loading
            val response = quizApiRepository.getStudySet(itemId)
            uiState = when (response) {
                is ApiResponse.Success -> {
                    StudySetUiState.Success(response.data)
                }

                is ApiResponse.Error -> {
                    StudySetUiState.Error
                }

                else -> {
                    StudySetUiState.Error
                }
            }
        }
    }

}