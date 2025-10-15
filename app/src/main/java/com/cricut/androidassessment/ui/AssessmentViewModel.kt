package com.cricut.androidassessment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cricut.androidassessment.data.QuizRepository
import com.cricut.androidassessment.model.QuizAnswer
import com.cricut.androidassessment.model.QuizQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AssessmentUiState(
    val isLoading: Boolean,
    val questions: List<QuizQuestion>,
    val currentQuestionIndex: Int,
    val answers: Map<String, QuizAnswer>,
    val errorMessage: String?
) {
    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(currentQuestionIndex)
    val isAtBeginning: Boolean
        get() = currentQuestionIndex == 0
    val isAtEnd: Boolean
        get() = questions.isNotEmpty() && currentQuestionIndex == questions.size - 1
}

@HiltViewModel
class AssessmentViewModel
@Inject constructor(private val quizRepository: QuizRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<AssessmentUiState>(
        AssessmentUiState(
            isLoading = true,
            questions = emptyList(),
            currentQuestionIndex = 0,
            answers = emptyMap(),
            errorMessage = null
        )
    )
    val uiState: StateFlow<AssessmentUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching {
                quizRepository.getQuestions()
            }.onSuccess { questions ->
                _uiState.update { previous ->
                    previous.copy(
                        isLoading = false,
                        questions = questions,
                        currentQuestionIndex = 0,
                        answers = previous.answers.filterKeys { questionsId -> questions.any { it.id == questionsId } })
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = throwable.message ?: "Unable to load questions.",
                    )
                }

            }
        }
    }
    fun goToNextQuestion() {
        _uiState.update { state ->
            if (state.isAtEnd) {
                state
            } else {
                state.copy(currentQuestionIndex = state.currentQuestionIndex + 1)
            }
        }
    }

    fun goToPreviousQuestion() {
        _uiState.update { state ->
            if (state.isAtBeginning) {
                state
            } else {
                state.copy(currentQuestionIndex = state.currentQuestionIndex - 1)
            }
        }
    }

    fun recordAnswer(questionId: String, answer: QuizAnswer) {
        _uiState.update { state ->
            val updatedAnswers = state.answers.toMutableMap()
            updatedAnswers[questionId] = answer
            state.copy(answers = updatedAnswers)
        }
    }
}
