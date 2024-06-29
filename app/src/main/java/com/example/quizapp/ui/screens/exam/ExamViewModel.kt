package com.example.quizapp.ui.screens.exam

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.constants.QuestionType
import com.example.quizapp.model.ExamResult
import com.example.quizapp.model.MultipleChoiceQuestion
import com.example.quizapp.model.Question
import com.example.quizapp.model.StudySetDetail
import com.example.quizapp.model.Term
import com.example.quizapp.model.TrueFalseQuestion
import com.example.quizapp.model.TypeAnswerQuestion
import com.example.quizapp.network.QuizApiRepository
import com.example.quizapp.network.request_model.StoreStudyRequest
import com.example.quizapp.network.response_model.ApiResponse
import com.example.quizapp.network.response_model.ResponseHandlerState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlin.random.Random

@HiltViewModel(assistedFactory = ExamViewModel.ExamViewModelFactory::class)
class ExamViewModel @AssistedInject constructor(
    private val quizApiRepository: QuizApiRepository, @Assisted private val studySet: StudySetDetail
) : ViewModel() {
    @AssistedFactory
    interface ExamViewModelFactory {
        fun create(studySet: StudySetDetail): ExamViewModel
    }

    private var _uiState = MutableStateFlow(ExamUiState(studySetDetail = studySet))
    val uiState: StateFlow<ExamUiState> = _uiState.asStateFlow()

    private val _storeResultResponse =
        MutableStateFlow<ResponseHandlerState<Unit>>(ResponseHandlerState.Init)
    val storeResultResponse: StateFlow<ResponseHandlerState<Unit>> =
        _storeResultResponse.asStateFlow()

    private val _studySetResponse =
        MutableStateFlow<ResponseHandlerState<StudySetDetail>>(ResponseHandlerState.Init)
    val studySetResponse: StateFlow<ResponseHandlerState<StudySetDetail>> =
        _studySetResponse.asStateFlow()

    init {
        randomQuestion()
    }

    fun fetchStudySet() {
        viewModelScope.launch {
            _studySetResponse.value = ResponseHandlerState.Loading
            val response = quizApiRepository.getStudySet(studySet.id)
            _studySetResponse.value = when (response) {
                is ApiResponse.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            studySetDetail = response.data,
                            currentTerm = 0,
                            currentQuestionResult = null,
                            questionList = mutableListOf(),
                            examResults = mutableListOf(),
                        )
                    }
                    Log.d("error", "success")
                    ResponseHandlerState.Success(response.data)
                }

                is ApiResponse.Error -> {
                    Log.d("error", "error: " + response.errorMsg)
                    ResponseHandlerState.Error(response.errorMsg)
                }

                is ApiResponse.Exception -> {
                    Log.d("error", "ex: " + response.errorMsg)
                    ResponseHandlerState.Error(response.errorMsg)
                }
            }
        }
    }

    fun sendResults() {
        viewModelScope.launch {
            val results = uiState.value.examResults.map {
                StoreStudyRequest(
                    it.question.termReferentId!!, it.isCorrect
                )
            }
            _storeResultResponse.value = ResponseHandlerState.Loading
            val response = quizApiRepository.storeStudyResults(results)
            _storeResultResponse.value = when (response) {
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

    fun setCurrentTerm(currentTerm: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                currentTerm = currentTerm,
            )
        }
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

    fun addResult(examResult: ExamResult) {
        _uiState.update { currentState ->
            currentState.copy(
                examResults = currentState.examResults.plus(examResult)
            )
        }
        setCurrentQuestionResult(examResult)
    }

    fun randomQuestion() {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
        val terms = selectTerms()
        val questions = terms.map { it ->
            val hasAudio = Random.nextBoolean()
            val questionType = Random.nextInt(1, 4)
            val question: Question
            when (questionType) {
                1 -> {
                    question = getMultipleChoiceQuestion(it, hasAudio)
                }

                2 -> {
                    question = getTrueFalseQuestion(it, hasAudio)
                }

                else -> {
                    question = getTypeAnswerQuestion(it, hasAudio)
                }
            }

            question
        }
        _uiState.update { currentState ->
            currentState.copy(
                questionList = questions, isLoading = false
            )
        }
    }

    fun selectTerms(): List<Term> {
        val results: MutableList<Term> = mutableListOf()
        val notStudyTerms = uiState.value.studySetDetail.terms.filter { it.status == 0 }

        val stillStudyTerms = uiState.value.studySetDetail.terms.filter { it.status == 1 }
        var stillStudySize = 5
        if (stillStudyTerms.size < 5) {
            stillStudySize = stillStudyTerms.size
        }
        results.addAll(stillStudyTerms.take(stillStudySize))

        var notStudySize = 10 - stillStudySize
        if (notStudyTerms.size < notStudySize) {
            notStudySize = notStudyTerms.size
            results.addAll(notStudyTerms.take(notStudySize))
            val masterTerms = uiState.value.studySetDetail.terms.filter { it.status == 2 }
            results.addAll(masterTerms.take(10 - stillStudySize - notStudySize))
        } else {
            results.addAll(notStudyTerms.take(notStudySize))
        }

        return results.shuffled()
    }

    fun getMultipleChoiceQuestion(term: Term, hasAudio: Boolean): Question {
        val answers = mutableListOf<String>();
        val correctIndex = Random.nextInt(1, 5)
        var index = Random.nextInt(0, uiState.value.studySetDetail.terms.size)
        for (i in 1..4) {
            if (correctIndex == i) {
                answers.add(
                    term.definition
                )
            } else {
                while (answers.contains(uiState.value.studySetDetail.terms[index].definition) || uiState.value.studySetDetail.terms[index].id == term.id) {
                    index = Random.nextInt(0, uiState.value.studySetDetail.terms.size)
                }
                answers.add(uiState.value.studySetDetail.terms[index].definition)
            }
        }
        val multipleChoiceQuestion = MultipleChoiceQuestion(
            answers = answers, correctAnswer = correctIndex - 1, question = term.term
        )
        return Question(
            hasAudio = hasAudio,
            audioText = if (hasAudio) term.term else null,
            audioLang = if (hasAudio) "us" else null,
            termReferentId = term.id,
            question = Json.parseToJsonElement(Json.encodeToString(multipleChoiceQuestion)).jsonObject,
            questionType = QuestionType.MultipleChoiceQuestion.value
        )
    }

    fun getTrueFalseQuestion(term: Term, hasAudio: Boolean): Question {
        var wrongTermIndex = 0
        do {
            wrongTermIndex = Random.nextInt(0, uiState.value.studySetDetail.terms.size)
        } while (uiState.value.studySetDetail.terms[wrongTermIndex].id == term.id)
        val correctAnswer = Random.nextBoolean()
        val question =
            if (correctAnswer) "${term.term} is ${term.definition} ?" else "${term.term} is ${uiState.value.studySetDetail.terms[wrongTermIndex].definition} ?"

        val trueFalseQuestion = TrueFalseQuestion(
            correctAnswer = correctAnswer, question = question
        )
        return Question(
            hasAudio = false,
            audioText = if (hasAudio) question else null,
            audioLang = if (hasAudio) "us" else null,
            termReferentId = term.id,
            question = Json.parseToJsonElement(Json.encodeToString(trueFalseQuestion)).jsonObject,
            questionType = QuestionType.TrueFalseQuestion.value
        )
    }

    fun getTypeAnswerQuestion(term: Term, hasAudio: Boolean): Question {
        val typeAnswerQuestion = TypeAnswerQuestion(
            question = term.definition,
            correctAnswer = term.term,
        )
        return Question(
            hasAudio = hasAudio,
            audioText = if (hasAudio) term.term else null,
            audioLang = if (hasAudio) "us" else null,
            termReferentId = term.id,
            question = Json.parseToJsonElement(Json.encodeToString(typeAnswerQuestion)).jsonObject,
            questionType = QuestionType.TypeAnswerQuestion.value
        )
    }
}