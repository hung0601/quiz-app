package com.example.quizapp.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.Search
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
class SearchViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
) : ViewModel() {
    private val _searchResult =
        MutableStateFlow<ResponseHandlerState<Search>>(ResponseHandlerState.Init)
    val searchResult: StateFlow<ResponseHandlerState<Search>> = _searchResult.asStateFlow()

    fun getResults(search: String) {
        viewModelScope.launch {
            _searchResult.value = ResponseHandlerState.Loading
            val response = quizApiRepository.search(search)
            _searchResult.value = when (response) {
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