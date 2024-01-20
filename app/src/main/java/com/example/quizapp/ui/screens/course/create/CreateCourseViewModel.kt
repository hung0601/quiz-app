package com.example.quizapp.ui.screens.course.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.Course
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
class CreateCourseViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
) : ViewModel() {
    private val _courseResponse =
        MutableStateFlow<ResponseHandlerState<Course>>(ResponseHandlerState.Init)
    val courseResponse: StateFlow<ResponseHandlerState<Course>> = _courseResponse.asStateFlow()

    fun resetState() {
        _courseResponse.value = ResponseHandlerState.Init
    }

    fun createCourse(title: String, description: String) {
        viewModelScope.launch {
            _courseResponse.value = ResponseHandlerState.Loading
            val response = quizApiRepository.createCourse(title, description)
            _courseResponse.value = when (response) {
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
