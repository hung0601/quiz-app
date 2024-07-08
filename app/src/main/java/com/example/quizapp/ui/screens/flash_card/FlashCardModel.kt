package com.example.quizapp.ui.screens.flash_card


import androidx.lifecycle.ViewModel
import com.example.quizapp.di.TextToSpeechService
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.model.Term
import com.example.quizapp.network.QuizApiRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale


@HiltViewModel(assistedFactory = FlashCardModel.FlashCardModelFactory::class)
class FlashCardModel @AssistedInject constructor(
    private val quizApiRepository: QuizApiRepository,
    private val textToSpeechService: TextToSpeechService,
    @Assisted private val studySet: StudySetDetail
) :
    ViewModel() {
    @AssistedFactory
    interface FlashCardModelFactory {
        fun create(studySet: StudySetDetail): FlashCardModel
    }

    private var _uiState =
        MutableStateFlow(FlashCardUiState(terms = studySet.terms.toMutableList()))
    val uiState: StateFlow<FlashCardUiState> = _uiState.asStateFlow()

    fun reset() {
        _uiState.update {
            FlashCardUiState(terms = studySet.terms.toMutableList())
        }
    }

    fun toggleOpen() {
        _uiState.update { currentState ->
            currentState.copy(
                isOpen = !currentState.isOpen
            )
        }
    }

    fun textToSpeech(text: String, lang: String) {
        textToSpeechService.setLang(Locale(lang))
        textToSpeechService.speak(text)
    }

    fun next() {
        if (_uiState.value.iterator.hasNext())
            _uiState.update { currentState ->
                var nextTerm = _uiState.value.iterator.next()
                if (nextTerm == currentState.currentTerm && currentState.iterator.hasNext())
                    nextTerm = _uiState.value.iterator.next()
                currentState.copy(
                    currentTerm = nextTerm
                )
            }
    }

    fun prev() {
        if (_uiState.value.iterator.hasPrevious()) {
            _uiState.update { currentState ->
                var prevTerm = _uiState.value.iterator.previous()
                if (prevTerm == currentState.currentTerm && currentState.iterator.hasPrevious())
                    prevTerm = _uiState.value.iterator.previous()
                currentState.copy(
                    currentTerm = prevTerm
                )
            }
        }
    }

    fun remove() {
        if (_uiState.value.terms.size > 0) {
            _uiState.update { currentState ->
                _uiState.value.iterator.remove()
                if (currentState.iterator.hasNext()) {
                    val nextTerm = _uiState.value.iterator.next()
                    currentState.copy(
                        currentTerm = nextTerm
                    )
                } else if (currentState.iterator.hasPrevious()) {
                    val prevTerm = _uiState.value.iterator.previous()
                    currentState.copy(
                        currentTerm = prevTerm
                    )
                } else {
                    currentState.copy()
                }
            }
            if (_uiState.value.terms.size == 0) {
                _uiState.update {
                    it.copy(
                        step = 1
                    )
                }
            }
        }
    }

    fun findIndex(term: Term): Int {
        return studySet.terms.indexOf(term)
    }

    fun findCurrentIndex(term: Term): Int {
        return _uiState.value.terms.indexOf(term)
    }
}