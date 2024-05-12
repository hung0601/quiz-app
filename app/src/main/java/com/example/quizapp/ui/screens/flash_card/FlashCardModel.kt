package com.example.quizapp.ui.screens.flash_card


import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import com.example.quizapp.network.QuizApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class FlashCardModel @Inject constructor(private val quizApiRepository: QuizApiRepository) :
    ViewModel() {

    private var _uiState = MutableStateFlow(FlashCardUiState())
    val uiState: StateFlow<FlashCardUiState> = _uiState.asStateFlow()
    private var textToSpeech: TextToSpeech? = null

    fun setCurrentTerm(currentTerm: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentTerm = currentTerm,
                isOpen = false
            )
        }
    }

    fun toggleOpen() {
        _uiState.update { currentState ->
            currentState.copy(
                isOpen = !currentState.isOpen
            )
        }
    }

    fun textToSpeech(context: Context, text: String) {
        textToSpeech = TextToSpeech(
            context
        ) {
            if (it == TextToSpeech.SUCCESS) {
                textToSpeech?.let { txtToSpeech ->
                    txtToSpeech.language = Locale.US
                    txtToSpeech.setSpeechRate(1.0f)
                    txtToSpeech.speak(
                        text,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )
                }
            }
        }
    }

}