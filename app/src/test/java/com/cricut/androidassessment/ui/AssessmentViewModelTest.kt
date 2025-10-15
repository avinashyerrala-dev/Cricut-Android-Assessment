package com.cricut.androidassessment.ui

import com.cricut.androidassessment.MainDispatcherRule
import com.cricut.androidassessment.data.QuizRepository
import com.cricut.androidassessment.model.QuizAnswer
import com.cricut.androidassessment.model.QuizQuestion
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AssessmentViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val sampleQuestions = listOf(
        QuizQuestion.TrueOrFalse(
            id = "question_true_false_1",
            question = "True or False: Android studio is official IDE for Android development"
        ),

        QuizQuestion.SingleChoice(
            id = "question_single_choice_1",
            question = "Which of the programming language is preferred for Android development",
            options = listOf("Swift", "Kotlin", "Objective-C", "Java")
        )
    )
    @Test
    fun `loads questions on init`() = runTest(dispatcherRule.testDispatcher) {
        val viewModel = AssessmentViewModel(FakeQuizRepository(sampleQuestions))

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.questions.size)
        assertEquals(0, state.currentQuestionIndex)
        assertTrue(state.errorMessage == null)
    }

    @Test
    fun `navigation preserves answers`() = runTest(dispatcherRule.testDispatcher) {
        val viewModel = AssessmentViewModel(FakeQuizRepository(sampleQuestions))
        advanceUntilIdle()

        viewModel.recordAnswer("question_true_false_1", QuizAnswer.BooleanAnswer(true))
        viewModel.goToNextQuestion()
        advanceUntilIdle()

        viewModel.recordAnswer("question_single_choice_1", QuizAnswer.SingleChoiceAnswer(2))
        viewModel.goToPreviousQuestion()

        val state = viewModel.uiState.value
        val firstAnswer = state.answers["question_true_false_1"] as? QuizAnswer.BooleanAnswer
        val secondAnswer = state.answers["question_single_choice_1"] as? QuizAnswer.SingleChoiceAnswer

        assertTrue(firstAnswer?.value == true)
        assertEquals(2, secondAnswer?.index)
    }

    private class FakeQuizRepository(
        private val questions: List<QuizQuestion>,
    ) : QuizRepository {
        override suspend fun getQuestions(): List<QuizQuestion> = questions
    }

}