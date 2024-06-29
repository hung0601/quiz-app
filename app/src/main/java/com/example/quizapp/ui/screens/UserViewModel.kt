package com.example.quizapp.ui.screens

import androidx.lifecycle.ViewModel
import com.example.quizapp.data.local.Session
import com.example.quizapp.data.local.SessionCache
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val sessionCache: SessionCache,
) : ViewModel() {


    val session get() = sessionCache.getActiveSession()


    fun saveSession(session: Session) {
        sessionCache.saveSession(
            session
        )
    }

    fun clearSession() {
        sessionCache.clearSession()
    }
}