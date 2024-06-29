package com.example.quizapp.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.quizapp.data.local.SessionCache
import com.example.quizapp.data.local.SessionCacheImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("session_prefs", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideSessionCache(sharedPreferences: SharedPreferences): SessionCache {
        return SessionCacheImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideAuth(sessionCache: SessionCache): String? {
        val session = sessionCache.getActiveSession()
        return session?.token
    }
}