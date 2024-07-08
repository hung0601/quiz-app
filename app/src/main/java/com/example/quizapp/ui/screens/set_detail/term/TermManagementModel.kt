package com.example.quizapp.ui.screens.set_detail.term

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.model.Term
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.request_model.StoreTermRequest
import com.example.quizapp.network.response_model.ResponseHandlerState
import com.example.quizapp.utils.handleResponseState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

@HiltViewModel(assistedFactory = TermManagementModel.TermManagementModelFactory::class)
class TermManagementModel @AssistedInject constructor(
    private val quizApiRepository: QuizApiRepository,
    private val savedStateHandle: SavedStateHandle,
    @Assisted private val studySet: StudySetDetail
) :
    ViewModel() {
    @AssistedFactory
    interface TermManagementModelFactory {
        fun create(studySet: StudySetDetail): TermManagementModel
    }

    private val _terms =
        MutableStateFlow(studySet.terms.toMutableList())
    val terms: StateFlow<List<Term>> = _terms.asStateFlow()

    private val _termResponse =
        MutableStateFlow<ResponseHandlerState<Term>>(ResponseHandlerState.Init)
    val termResponse: StateFlow<ResponseHandlerState<Term>> = _termResponse.asStateFlow()

    private val _deleteResponse =
        MutableStateFlow<ResponseHandlerState<Unit>>(ResponseHandlerState.Init)
    val deleteResponse: StateFlow<ResponseHandlerState<Unit>> = _deleteResponse.asStateFlow()

    private val _addTermResponse =
        MutableStateFlow<ResponseHandlerState<Term>>(ResponseHandlerState.Init)
    val addTermResponse: StateFlow<ResponseHandlerState<Term>> = _addTermResponse.asStateFlow()

    private val setId: Int = checkNotNull(savedStateHandle.get<Int>("id"))

    fun resetState() {
        _addTermResponse.value = ResponseHandlerState.Init
    }

    fun updateTerm(
        term: Term,
        formData: StoreTermRequest,
    ) {
        viewModelScope.launch {
            _termResponse.value = ResponseHandlerState.Loading
            val response = quizApiRepository.updateTerm(term.id, formData, false)
            _termResponse.value = handleResponseState(response)
            if (_termResponse.value is ResponseHandlerState.Success) {
                _terms.update {
                    val index = it.indexOf(term)
                    it[index] = (_termResponse.value as ResponseHandlerState.Success<Term>).data
                    it
                }
            }
        }
    }

    fun deleteTerm(term: Term) {
        viewModelScope.launch {
            _deleteResponse.value = ResponseHandlerState.Loading
            val response = quizApiRepository.deleteTerm(term.id)
            _deleteResponse.value = handleResponseState(response)
            if (_deleteResponse.value is ResponseHandlerState.Success) {
                _terms.update {
                    it.minus(term).toMutableList()
                }
            }
        }
    }

    fun addTerm(
        image: File?,
        term: String,
        definition: String,
    ) {
        viewModelScope.launch {
            _addTermResponse.value = ResponseHandlerState.Loading
            val response =
                quizApiRepository.addTerm(StoreTermRequest(image, term, definition, setId))
            _addTermResponse.value = handleResponseState(response)
            if (_addTermResponse.value is ResponseHandlerState.Success) {
                _terms.update {
                    it.add((_addTermResponse.value as ResponseHandlerState.Success<Term>).data)
                    it
                }
            }
        }
    }
}