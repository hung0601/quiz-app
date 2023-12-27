package com.example.quizapp.di

import com.example.quizapp.data.NetworkQuizApiRepository
import com.example.quizapp.data.QuizApiRepository
import com.example.quizapp.network.QuizApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("http://192.168.1.7:8000/api/")
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