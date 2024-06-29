package com.example.quizapp.data.local

interface SessionCache {
    fun saveSession(session: Session)

    fun getActiveSession(): Session?

    fun clearSession()
}