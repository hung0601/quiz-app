package com.example.quizapp.ui.screens.course.detail

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
import javax.inject.Inject


@HiltViewModel
class CreatedSetViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
) : ViewModel() {
    private val _setList =
        MutableStateFlow<ResponseHandlerState<List<StudySet>>>(ResponseHandlerState.Loading)
    val setList: StateFlow<ResponseHandlerState<List<StudySet>>> = _setList.asStateFlow()


    private val _addSetResponse =
        MutableStateFlow<ResponseHandlerState<Unit>>(ResponseHandlerState.Init)
    val addSetResponse: StateFlow<ResponseHandlerState<Unit>> = _addSetResponse.asStateFlow()

    fun resetState() {
        _addSetResponse.value = ResponseHandlerState.Init
    }

    init {
        getSetList()
    }

    fun getSetList() {
        viewModelScope.launch {
            _setList.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getCreatedSets()
            _setList.value = when (response) {
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

    fun addSets(courseId: Int, studySetIds: List<Int>) {
        viewModelScope.launch {
            _addSetResponse.value = ResponseHandlerState.Loading
            val response = quizApiRepository.addStudySets(courseId, studySetIds)
            _addSetResponse.value = when (response) {
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