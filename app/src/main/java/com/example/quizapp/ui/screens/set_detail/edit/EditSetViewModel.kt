package com.example.quizapp.ui.screens.set_detail.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.StudySet
import com.example.quizapp.model.Topic
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.request_model.StoreStudySetRequest
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.utils.handleResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditSetViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _setResponse =
        MutableStateFlow<ResponseHandlerState<StudySet>>(ResponseHandlerState.Init)
    val setResponse: StateFlow<ResponseHandlerState<StudySet>> = _setResponse.asStateFlow()

    private val _topics =
        MutableStateFlow<List<Topic>>(emptyList())
    val topics: StateFlow<List<Topic>> = _topics.asStateFlow()


    private val setId: Int = checkNotNull(savedStateHandle.get<Int>("id"))
    fun resetState() {
        _setResponse.value = ResponseHandlerState.Init
    }

    fun updateSet(
        formData: StoreStudySetRequest
    ) {
        viewModelScope.launch {
            _setResponse.value = ResponseHandlerState.Loading
            val response = quizApiRepository.updateSet(setId, formData)
            _setResponse.value = handleResponseState(response)
        }
    }

    fun addTopic(topic: Topic) {
        _topics.update {
            it.plus(topic)
        }
    }

    fun removeTopic(topic: Topic) {
        _topics.update {
            it.minus(topic)
        }
    }
}