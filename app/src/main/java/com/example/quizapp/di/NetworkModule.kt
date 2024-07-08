package com.example.quizapp.di

import com.example.quizapp.data.local.SessionCache
import com.example.quizapp.network.NetworkQuizApiRepository
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.QuizApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(sessionCache: SessionCache): Retrofit {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val bearerToken = sessionCache.getActiveSession()?.token ?: ""
                val builder = original.newBuilder()
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer $bearerToken")
                val request = builder.build()
                chain.proceed(request)
            }
            .build()
        return Retrofit.Builder()
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json".toMediaType()))
            .baseUrl("http://127.0.0.1:8000/api/")
            .client((httpClient))
            .build()
    }


    @Singleton
    @Provides
    fun provideQuizApiService(retrofit: Retrofit): QuizApiService {
        return retrofit.create(QuizApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideQuizApiRepository(quizApiService: QuizApiService): QuizApiRepository {
        return NetworkQuizApiRepository(quizApiService)
    }
}