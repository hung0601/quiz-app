package com.example.quizapp.utils

import com.example.quizapp.data.local.SessionCache
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionCache: SessionCache,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionCache.getActiveSession()?.token
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}