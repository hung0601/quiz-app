package com.example.quizapp.utils

import android.util.Log
import com.example.quizapp.network.response_model.ApiResponse
import com.example.quizapp.network.response_model.ResponseHandlerState

fun <T : Any> handleResponseState(response: ApiResponse<T>): ResponseHandlerState<T> {
    val result = when (response) {
        is ApiResponse.Success -> {
            ResponseHandlerState.Success(response.data)
        }

        is ApiResponse.Error -> {
            Log.d("Error", response.errorMsg)
            ResponseHandlerState.Error(response.errorMsg)
        }

        is ApiResponse.Exception -> {
            Log.d("Error", response.errorMsg)
            ResponseHandlerState.Error(response.errorMsg)
        }
    }
    return result
}