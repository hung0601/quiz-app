package com.example.quizapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.Course
import com.example.quizapp.model.CourseInvite
import com.example.quizapp.model.MyProfile
import com.example.quizapp.model.StudySet
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.utils.handleResponseState
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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        getCourseList()
        getStudySetList()
        getCreatorList()
        getInvites()
    }

    fun fetchData() {
        if (!_isLoading.value) {
            _isLoading.value = true
            getCourseList()
            getStudySetList()
            getCreatorList()
            getInvites()
            _isLoading.value = false
        }
    }


    fun getInvites() {
        viewModelScope.launch {
            _inviteList.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getInvites()
            _inviteList.value = handleResponseState(response)
        }
    }

    fun getStudySetList() {
        viewModelScope.launch {
            _studySetList.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getStudySets()
            _studySetList.value = handleResponseState(response)
        }
    }

    fun getCourseList() {
        viewModelScope.launch {
            _courseList.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getCourses()
            _courseList.value = handleResponseState(response)
        }
    }

    fun getCreatorList() {
        viewModelScope.launch {
            _creatorList.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getTopCreators()
            _creatorList.value = handleResponseState(response)
        }
    }
}
