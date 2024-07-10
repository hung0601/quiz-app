package com.example.quizapp.ui.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.local.SessionCache
import com.example.quizapp.model.Course
import com.example.quizapp.model.CreatorProfile
import com.example.quizapp.model.StudySet
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.utils.handleResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatorProfileViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
    private val savedStateHandle: SavedStateHandle,
    private val sessionCache: SessionCache,
) : ViewModel() {
    private val _userProfile =
        MutableStateFlow<ResponseHandlerState<CreatorProfile>>(ResponseHandlerState.Loading)
    val userProfile: StateFlow<ResponseHandlerState<CreatorProfile>> = _userProfile.asStateFlow()

    private val _studySets =
        MutableStateFlow<ResponseHandlerState<List<StudySet>>>(ResponseHandlerState.Loading)
    val studySets: StateFlow<ResponseHandlerState<List<StudySet>>> = _studySets.asStateFlow()

    private val _courses =
        MutableStateFlow<ResponseHandlerState<List<Course>>>(ResponseHandlerState.Loading)
    val courses: StateFlow<ResponseHandlerState<List<Course>>> = _courses.asStateFlow()

    private val _followers =
        MutableStateFlow<ResponseHandlerState<List<CreatorProfile>>>(ResponseHandlerState.Loading)
    val followers: StateFlow<ResponseHandlerState<List<CreatorProfile>>> = _followers.asStateFlow()

    private val _followRes =
        MutableStateFlow<ResponseHandlerState<Unit>>(ResponseHandlerState.Loading)
    val followRes: StateFlow<ResponseHandlerState<Unit>> = _followRes.asStateFlow()

    private val userId: Int = checkNotNull(savedStateHandle.get<Int>("id"))

    val currentUser = sessionCache.getActiveSession()?.user

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            val profileResponse = async { quizApiRepository.getProfile(userId) }
            val studySetResponse = async { quizApiRepository.getSetsByUser(userId) }
            val courseResponse = async { quizApiRepository.getCoursesByUser(userId) }
            val followersResponse = async { quizApiRepository.getFollowers(userId) }

            _userProfile.value = handleResponseState(profileResponse.await())
            _studySets.value = handleResponseState(studySetResponse.await())
            _courses.value = handleResponseState(courseResponse.await())
            _followers.value = handleResponseState(followersResponse.await())
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            if (!_isLoading.value) {
                _isLoading.value = true
                val studySetResponse = async { quizApiRepository.getSetsByUser(userId) }
                val courseResponse = async { quizApiRepository.getCoursesByUser(userId) }
                val followersResponse = async { quizApiRepository.getFollowers(userId) }

                _studySets.value = handleResponseState(studySetResponse.await())
                _courses.value = handleResponseState(courseResponse.await())
                _followers.value = handleResponseState(followersResponse.await())
                _isLoading.value = false
            }
        }
    }

    fun follow(userId: Int) {
        viewModelScope.launch {
            val followRes = async { quizApiRepository.followUser(userId) }
            val response = handleResponseState(followRes.await())
            if (response is ResponseHandlerState.Success) {
                _userProfile.update { currentUser ->
                    ResponseHandlerState.Success(
                        (currentUser as ResponseHandlerState.Success<CreatorProfile>).data.copy(
                            isFollowing = true,
                            followersCount = (currentUser.data.followersCount ?: 0) + 1,
                        )
                    )
                }
            }
        }
    }

    fun unFollow(userId: Int) {
        viewModelScope.launch {
            val followRes = async { quizApiRepository.unFollow(userId) }
            val response = handleResponseState(followRes.await())
            if (response is ResponseHandlerState.Success) {
                _userProfile.update { currentUser ->
                    ResponseHandlerState.Success(
                        (currentUser as ResponseHandlerState.Success<CreatorProfile>).data.copy(
                            isFollowing = false,
                            followersCount = (currentUser.data.followersCount ?: 0) - 1,
                        )
                    )
                }
            }
        }
    }
}