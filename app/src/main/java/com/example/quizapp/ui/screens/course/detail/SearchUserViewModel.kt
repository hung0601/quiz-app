package com.example.quizapp.ui.screens.course.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.Profile
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
class SearchUserViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
) : ViewModel() {
    private val _searchUsers =
        MutableStateFlow<ResponseHandlerState<List<Profile>>>(ResponseHandlerState.Init)
    val searchUsers: StateFlow<ResponseHandlerState<List<Profile>>> = _searchUsers.asStateFlow()

    private var _inviteResponse =
        MutableStateFlow<ResponseHandlerState<Unit>>(ResponseHandlerState.Init)
    val inviteResponse: StateFlow<ResponseHandlerState<Unit>> = _inviteResponse.asStateFlow()

    fun resetState() {
        _inviteResponse.value = ResponseHandlerState.Init
    }

    fun inviteMember(
        courseId: Int, participantId: Int
    ) {
        viewModelScope.launch {
            _inviteResponse.value = ResponseHandlerState.Loading
            val response = quizApiRepository.inviteMember(courseId, participantId, "invite")
            _inviteResponse.value = when (response) {
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

    fun searchCourseUsers(courseId: Int, search: String) {
        if (search.isNotEmpty()) {
            viewModelScope.launch {
                val response = quizApiRepository.searchCourseUsers(courseId, search)
                Log.d("response", response.toString())
                _searchUsers.value = when (response) {
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
}