package com.example.quizapp.ui.screens.library

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.Course
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
class LibraryViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _studySetList =
        MutableStateFlow<ResponseHandlerState<List<StudySet>>>(ResponseHandlerState.Loading)
    val studySetList: StateFlow<ResponseHandlerState<List<StudySet>>> = _studySetList.asStateFlow()

    private val _courseList =
        MutableStateFlow<ResponseHandlerState<List<Course>>>(ResponseHandlerState.Loading)
    val courseList: StateFlow<ResponseHandlerState<List<Course>>> = _courseList.asStateFlow()

    val tabId: Int = checkNotNull(savedStateHandle.get<Int>("tabId"))

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        getCourseList()
        getStudySetList()
    }

    fun fetchData() {
        if (!_isLoading.value) {
            _isLoading.value = true
            getCourseList()
            getStudySetList()
            _isLoading.value = false
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
}