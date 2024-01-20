package com.example.quizapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.example.quizapp.data.session.SessionCache
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionCache: SessionCache,
) : ViewModel() {
    val currentUser = sessionCache.getActiveSession()?.user
}
