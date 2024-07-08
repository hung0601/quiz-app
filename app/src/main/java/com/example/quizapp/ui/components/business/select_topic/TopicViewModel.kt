package com.example.quizapp.ui.components.business.select_topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.Topic
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
class TopicViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
) : ViewModel() {
    private val _topicList =
        MutableStateFlow<ResponseHandlerState<List<Topic>>>(ResponseHandlerState.Init)
    val topicList: StateFlow<ResponseHandlerState<List<Topic>>> = _topicList.asStateFlow()

    init {
        getTopicList()
    }

    fun getTopicList(search: String = "") {
        viewModelScope.launch {
            _topicList.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getTopics(search)
            _topicList.value = handleResponseState(response)
        }
    }
}