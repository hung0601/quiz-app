package com.example.quizapp.ui.components.business.study_set

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.model.Member
import com.example.quizapp.model.MyProfile
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
class MemberViewModel @Inject constructor(
    private val quizApiRepository: QuizApiRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _memberList =
        MutableStateFlow<ResponseHandlerState<List<Member>>>(ResponseHandlerState.Init)
    val memberList: StateFlow<ResponseHandlerState<List<Member>>> = _memberList.asStateFlow()

    private val _searchUsers =
        MutableStateFlow<ResponseHandlerState<List<MyProfile>>>(ResponseHandlerState.Init)
    val searchUsers: StateFlow<ResponseHandlerState<List<MyProfile>>> = _searchUsers.asStateFlow()

    private var _inviteResponse =
        MutableStateFlow<ResponseHandlerState<Unit>>(ResponseHandlerState.Init)
    val inviteResponse: StateFlow<ResponseHandlerState<Unit>> = _inviteResponse.asStateFlow()

    private var _deleteResponse =
        MutableStateFlow<ResponseHandlerState<Unit>>(ResponseHandlerState.Init)
    val deleteResponse: StateFlow<ResponseHandlerState<Unit>> = _deleteResponse.asStateFlow()

    private val itemId: Int = checkNotNull(savedStateHandle.get<Int>("id"))

    init {
        getSetMember()
    }

    fun resetState() {
        _inviteResponse.value = ResponseHandlerState.Init
    }

    fun getSetMember() {
        viewModelScope.launch {
            _memberList.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getStudySetMembers(itemId)
            _memberList.value = handleResponseState(response)
        }
    }

    fun deleteMember(userId: Int) {
        viewModelScope.launch {
            val response = quizApiRepository.removeSetMember(itemId, userId)
            _deleteResponse.value = handleResponseState(response)
            getSetMember()
        }
    }

    fun inviteMember(
        setId: Int, participantId: Int, accessLevel: Int
    ) {
        viewModelScope.launch {
            _inviteResponse.value = ResponseHandlerState.Loading
            val response = quizApiRepository.inviteToSet(setId, participantId, accessLevel)
            _inviteResponse.value = handleResponseState(response)
        }
    }

    fun searchUsers(setId: Int, search: String) {
        if (search.isNotEmpty()) {
            viewModelScope.launch {
                val response = quizApiRepository.searchSetUsers(setId, search)
                _searchUsers.value = handleResponseState(response)
            }
        }
    }
}