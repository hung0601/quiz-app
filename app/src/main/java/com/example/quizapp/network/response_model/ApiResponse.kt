package com.example.quizapp.network.response_model

sealed class ApiResponse<out T : Any> {
    data class Success<out T : Any>(val data: T) : ApiResponse<T>()
    data class Error(val errorMsg: String) : ApiResponse<Nothing>()
    data class Exception(val errorMsg: String) : ApiResponse<Nothing>()
}