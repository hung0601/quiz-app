package com.example.quizapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.local.Session
import com.example.quizapp.data.local.SessionCache
import com.example.quizapp.model.Token
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.request_model.LoginRequest
import com.example.quizapp.network.response_model.ApiResponse
import com.example.quizapp.network.response_model.ResponseHandlerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionCache: SessionCache,
    private val quizApiRepository: QuizApiRepository,
) : ViewModel() {
    private val _loginResponse =
        MutableStateFlow<ResponseHandlerState<Token>>(ResponseHandlerState.Init)
    val loginResponse: StateFlow<ResponseHandlerState<Token>> = _loginResponse.asStateFlow()

    fun resetState() {
        _loginResponse.value = ResponseHandlerState.Init
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResponse.value = ResponseHandlerState.Loading
            val response = quizApiRepository.login(LoginRequest(email, password))
            _loginResponse.value = when (response) {
                is ApiResponse.Success -> {
                    sessionCache.saveSession(
                        Session(
                            response.data.accessToken,
                            1L,
                            response.data.user
                        )
                    )
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
