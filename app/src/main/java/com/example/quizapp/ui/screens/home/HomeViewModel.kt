package com.example.quizapp.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.Course
import com.example.quizapp.model.CourseInvite
import com.example.quizapp.model.MyProfile
import com.example.quizapp.model.StudySet
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.response_model.ApiResponse
import com.example.quizapp.network.response_model.ResponseHandlerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
) : ViewModel() {
    private val _studySetList =
        MutableStateFlow<ResponseHandlerState<List<StudySet>>>(ResponseHandlerState.Loading)
    val studySetList: StateFlow<ResponseHandlerState<List<StudySet>>> = _studySetList.asStateFlow()

    private val _courseList =
        MutableStateFlow<ResponseHandlerState<List<Course>>>(ResponseHandlerState.Loading)
    val courseList: StateFlow<ResponseHandlerState<List<Course>>> = _courseList.asStateFlow()

    private val _creatorList =
        MutableStateFlow<ResponseHandlerState<List<MyProfile>>>(ResponseHandlerState.Loading)
    val creatorList: StateFlow<ResponseHandlerState<List<MyProfile>>> = _creatorList.asStateFlow()

    private val _inviteList =
        MutableStateFlow<ResponseHandlerState<List<CourseInvite>>>(ResponseHandlerState.Loading)
    val inviteList: StateFlow<ResponseHandlerState<List<CourseInvite>>> = _inviteList.asStateFlow()


    init {
        getCourseList()
        getStudySetList()
        getCreatorList()
        getInvites()
    }


    fun getInvites() {
        viewModelScope.launch {
            _inviteList.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getInvites()
            Log.d("response", response.toString())
            _inviteList.value = when (response) {
                is ApiResponse.Success -> {
                    ResponseHandlerState.Success(response.data)
                }

                is ApiResponse.Error -> {
                    ResponseHandlerState.Error(response.errorMsg)
                }

                is ApiResponse.Exception -> {
                    ResponseHandlerState.Error(response.errorMsg)
                }
            }
        }
    }

    fun getStudySetList() {
        viewModelScope.launch {
            _studySetList.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getStudySets()
            _studySetList.value = when (response) {
                is ApiResponse.Success -> {
                    ResponseHandlerState.Success(response.data)
                }

                is ApiResponse.Error -> {
                    ResponseHandlerState.Error(response.errorMsg)
                }

                is ApiResponse.Exception -> {
                    ResponseHandlerState.Error(response.errorMsg)
                }
            }
        }
    }

    fun getCourseList() {
        viewModelScope.launch {
            _courseList.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getCourses()
            _courseList.value = when (response) {
                is ApiResponse.Success -> {
                    ResponseHandlerState.Success(response.data)
                }

                is ApiResponse.Error -> {
                    ResponseHandlerState.Error(response.errorMsg)
                }

                is ApiResponse.Exception -> {
                    ResponseHandlerState.Error(response.errorMsg)
                }
            }
        }
    }

    fun getCreatorList() {
        viewModelScope.launch {
            _creatorList.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getTopCreators()
            _creatorList.value = when (response) {
                is ApiResponse.Success -> {
                    ResponseHandlerState.Success(response.data)
                }

                is ApiResponse.Error -> {
                    ResponseHandlerState.Error(response.errorMsg)
                }

                is ApiResponse.Exception -> {
                    ResponseHandlerState.Error(response.errorMsg)
                }
            }
        }
    }
}
