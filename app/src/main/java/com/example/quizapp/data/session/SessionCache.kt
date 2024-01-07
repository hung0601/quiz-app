package com.example.quizapp.data.session

interface SessionCache {
    fun saveSession(session: Session)

    fun getActiveSession(): Session?

    fun clearSession()
}