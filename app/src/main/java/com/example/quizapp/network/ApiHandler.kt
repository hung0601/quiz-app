package com.example.quizapp.network

import com.example.quizapp.network.response_model.ApiResponse
import com.example.quizapp.network.response_model.ErrorResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.HttpException
import retrofit2.Response

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val adapter = moshi.adapter(ErrorResponse::class.java)

interface ApiHandler {
    suspend fun <T : Any> handleApi(
        execute: suspend () -> Response<T>
    ): ApiResponse<T> {
        return try {
            val response = execute()
            //Log.d("response","$response")
            val body = response.body()
            //Log.d("response", "$body")
            if (response.isSuccessful && body != null) {
                ApiResponse.Success(body)
            } else {
                val err = adapter.fromJson(
                    response.errorBody()?.charStream()?.readText() ?: "{message:'unknown error'}"
                )
                ApiResponse.Error(err?.message ?: "unknown error")
            }
        } catch (e: HttpException) {
            ApiResponse.Error(e.message())
        } catch (e: Throwable) {
            ApiResponse.Exception(e.message ?: "unknown error")
        }

    }
}