package com.example.quizapp.util

import com.example.quizapp.network.response_model.ApiResponse
import com.example.quizapp.network.response_model.ResponseHandlerState

fun <T : Any> handleResponseState(response: ApiResponse<T>): ResponseHandlerState<T> {
    val result = when (response) {
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
    return result
}