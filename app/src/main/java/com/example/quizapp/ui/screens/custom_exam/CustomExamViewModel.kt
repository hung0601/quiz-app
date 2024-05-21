package com.example.quizapp.ui.screens.custom_exam

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.ExamDetail
import com.example.quizapp.model.ExamResult
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.request_model.SubmitExamRequest
import com.example.quizapp.network.response_model.ApiResponse
import com.example.quizapp.network.response_model.ResponseHandlerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomExamViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var _uiState = MutableStateFlow(CustomExamUiState())
    val uiState: StateFlow<CustomExamUiState> = _uiState.asStateFlow()
    private val testId: Int = checkNotNull(savedStateHandle.get<Int>("id"))

    private val _examResponse =
        MutableStateFlow<ResponseHandlerState<ExamDetail>>(ResponseHandlerState.Init)
    val examResponse: StateFlow<ResponseHandlerState<ExamDetail>> = _examResponse.asStateFlow()

    private val _submitResponse =
        MutableStateFlow<ResponseHandlerState<Unit>>(ResponseHandlerState.Init)
    val submitResponse: StateFlow<ResponseHandlerState<Unit>> = _submitResponse.asStateFlow()

    init {
        getExam()
    }

    fun reset() {
        _uiState.update {
            CustomExamUiState(examDetail = it.examDetail, questionList = it.questionList)
        }
    }

    fun setCurrentQuestion(currentQuestion: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentQuestion = currentQuestion
            )
        }
    }

    fun addResult(examResult: ExamResult) {
        _uiState.update { currentState ->
            currentState.copy(
                examResults = currentState.examResults.plus(examResult)
            )
        }
        setCurrentQuestionResult(examResult)
    }

    fun setLoading(loading: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = loading,
            )
        }
    }

    fun setCurrentQuestionResult(currentQuestionResult: ExamResult) {
        _uiState.update { currentState ->
            currentState.copy(
                currentQuestionResult = currentQuestionResult
            )
        }
    }

    fun submitExam() {
        viewModelScope.launch {
            val results = uiState.value.examResults.map {
                SubmitExamRequest(it.question.id, it.isCorrect, it.correctAnswer.toString())
            }
            _submitResponse.value = ResponseHandlerState.Loading
            val response = quizApiRepository.submitExam(uiState.value.examDetail!!.id, results)
            _submitResponse.value = when (response) {
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

    fun getExam() {
        viewModelScope.launch {
            _examResponse.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getExam(testId)
            _examResponse.value = when (response) {
                is ApiResponse.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            examDetail = response.data, questionList = response.data.questions
                        )
                    }
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