package com.example.quizapp.ui.screens.course.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.session.SessionCache
import com.example.quizapp.model.CourseDetail
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
class CourseViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
    private val savedStateHandle: SavedStateHandle,
    private val sessionCache: SessionCache,
) : ViewModel() {
    private val _courseDetail =
        MutableStateFlow<ResponseHandlerState<CourseDetail>>(ResponseHandlerState.Loading)
    val courseDetail: StateFlow<ResponseHandlerState<CourseDetail>> = _courseDetail.asStateFlow()
    private val itemId: Int = checkNotNull(savedStateHandle.get<Int>("id"))

    val currentUser = sessionCache.getActiveSession()?.user

    init {
        getCourse()
    }

    fun getCourse() {
        viewModelScope.launch {
            _courseDetail.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getCourse(itemId)
            _courseDetail.value = when (response) {
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