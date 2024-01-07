package com.example.quizapp.network.response_model

sealed class ResponseHandlerState<out T : Any> {
    object Init : ResponseHandlerState<Nothing>()
    data class Success<out T : Any>(val data: T) : ResponseHandlerState<T>()
    data class Error(val errorMsg: String) : ResponseHandlerState<Nothing>()
    object Loading : ResponseHandlerState<Nothing>()
}