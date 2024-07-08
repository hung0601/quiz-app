package com.example.quizapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.local.SessionCache
import com.example.quizapp.model.CreatorProfile
import com.example.quizapp.model.MyProfile
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.utils.handleResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionCache: SessionCache,
    private val quizApiRepository: QuizApiRepository,
) : ViewModel() {
    val currentUser = sessionCache.getActiveSession()?.user

    private val _userProfile =
        MutableStateFlow<ResponseHandlerState<MyProfile>>(ResponseHandlerState.Loading)
    val userProfile: StateFlow<ResponseHandlerState<MyProfile>> = _userProfile.asStateFlow()

    private val _followers =
        MutableStateFlow<ResponseHandlerState<List<CreatorProfile>>>(ResponseHandlerState.Loading)
    val followers: StateFlow<ResponseHandlerState<List<CreatorProfile>>> = _followers.asStateFlow()

    private val _followings =
        MutableStateFlow<ResponseHandlerState<List<CreatorProfile>>>(ResponseHandlerState.Loading)
    val followings: StateFlow<ResponseHandlerState<List<CreatorProfile>>> =
        _followings.asStateFlow()

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            val profileResponse = async { quizApiRepository.getMyProfile() }
            _userProfile.value = handleResponseState(profileResponse.await())

            val followersResponse = async { quizApiRepository.getFollowers() }
            val followingsResponse = async { quizApiRepository.getFollowings() }

            _followers.value = handleResponseState(followersResponse.await())
            _followings.value = handleResponseState(followingsResponse.await())
        }
    }
}
