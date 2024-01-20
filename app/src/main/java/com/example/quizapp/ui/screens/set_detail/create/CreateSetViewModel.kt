package com.example.quizapp.ui.screens.set_detail.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.StudySet
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.response_model.ApiResponse
import com.example.quizapp.network.response_model.ResponseHandlerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class CreateSetViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _setResponse =
        MutableStateFlow<ResponseHandlerState<StudySet>>(ResponseHandlerState.Init)
    val setResponse: StateFlow<ResponseHandlerState<StudySet>> = _setResponse.asStateFlow()

    private val courseId: Int = checkNotNull(savedStateHandle.get<Int>("courseId"))
    fun resetState() {
        _setResponse.value = ResponseHandlerState.Init
    }

    fun createSet(
        image: File,
        title: String,
        description: String,
    ) {
        viewModelScope.launch {
            _setResponse.value = ResponseHandlerState.Loading
            val courseIdIn = if (courseId > 0) courseId else null
            val response = quizApiRepository.createSet(image, title, description, courseIdIn)
            _setResponse.value = when (response) {
                is ApiResponse.Success -> {
                    ResponseHandlerState.Success(response.data)
                }

                is ApiResponse.Error -> {
                    ResponseHandlerState.Error(response.errorMsg)
                }

                is ApiResponse.Exception -> {
                    ResponseHandlerState.Error(response.errorMsg)
                }
            }
        }
    }
}
