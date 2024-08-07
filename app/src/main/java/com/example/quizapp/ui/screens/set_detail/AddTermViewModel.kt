package com.example.quizapp.ui.screens.set_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.Term
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.request_model.StoreTermRequest
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.utils.handleResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class AddTermViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
) : ViewModel() {
    private val _addTermResponse =
        MutableStateFlow<ResponseHandlerState<Term>>(ResponseHandlerState.Init)
    val addTermResponse: StateFlow<ResponseHandlerState<Term>> = _addTermResponse.asStateFlow()

    fun resetState() {
        _addTermResponse.value = ResponseHandlerState.Init
    }

    fun createSet(
        image: File,
        term: String,
        definition: String,
        studySetId: Int
    ) {
        viewModelScope.launch {
            _addTermResponse.value = ResponseHandlerState.Loading
            val response =
                quizApiRepository.addTerm(StoreTermRequest(image, term, definition, studySetId))
            _addTermResponse.value = handleResponseState(response)
        }
    }
}
