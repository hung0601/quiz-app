package com.example.quizapp.ui.screens.set_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.local.SessionCache
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.response_model.ApiResponse
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.utils.handleResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _voteResponse =
        MutableStateFlow<ResponseHandlerState<Float>>(ResponseHandlerState.Init)
    val voteResponse: StateFlow<ResponseHandlerState<Float>> =
        _voteResponse.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        getStudySet()
    }

    fun fetchData() {
        if (!_isLoading.value) {
            _isLoading.value = true
            getStudySet()
            _isLoading.value = false
        }
    }

    fun updateVote(star: Float) {
        if (uiState is StudySetUiState.Success) {
            val copySet = (uiState as StudySetUiState.Success).studySet.copy(votesAvgStar = star)
            uiState = (uiState as StudySetUiState.Success).copy(studySet = copySet)
        }
    }

    fun sendVote(star: Int) {
        viewModelScope.launch {
            _voteResponse.value = ResponseHandlerState.Loading
            val response = quizApiRepository.voteSet(studySetId = itemId, star = star)
            _voteResponse.value = handleResponseState(response)
        }
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