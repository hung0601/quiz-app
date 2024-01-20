package com.example.quizapp.ui.screens.notification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.CourseInvite
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
class NotificationViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
) : ViewModel() {

    private val _inviteList =
        MutableStateFlow<ResponseHandlerState<List<CourseInvite>>>(ResponseHandlerState.Loading)
    val inviteList: StateFlow<ResponseHandlerState<List<CourseInvite>>> = _inviteList.asStateFlow()


    private var _acceptResponse =
        MutableStateFlow<ResponseHandlerState<Boolean>>(ResponseHandlerState.Init)
    val acceptResponse: StateFlow<ResponseHandlerState<Boolean>> = _acceptResponse.asStateFlow()

    var courseId = 0

    init {
        getInvites()
    }

    fun resetState() {
        _acceptResponse.value = ResponseHandlerState.Init
    }

    fun acceptInvite(
        courseId: Int, isAccept: Boolean
    ) {
        this.courseId = courseId
        viewModelScope.launch {
            _acceptResponse.value = ResponseHandlerState.Loading
            val response = quizApiRepository.acceptInvite(courseId, isAccept)
            _acceptResponse.value = when (response) {
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

    fun getInvites() {
        viewModelScope.launch {
            _inviteList.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getInvites()
            Log.d("response", response.toString())
            _inviteList.value = when (response) {
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
