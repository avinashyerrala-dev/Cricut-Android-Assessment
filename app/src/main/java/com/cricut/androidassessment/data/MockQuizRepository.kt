package com.cricut.androidassessment.data

import com.cricut.androidassessment.model.QuizQuestion
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockQuizRepository @Inject constructor() : QuizRepository {
    override suspend fun getQuestions(): List<QuizQuestion> {
        delay(1000)
        val mockQuestions = listOf(
            QuizQuestion.TrueOrFalse(
                id = "question_true_false_1",
                question = "True or False: Android studio is official IDE for Android development"
            ),

            QuizQuestion.SingleChoice(
                id = "question_single_choice_1",
                question = "Which of the programming language is preferred for Android development",
                options = listOf("Swift", "Kotlin", "Objective-C", "Java")
            ),
//            QuizQuestion.MultipleChoice(
//                id = "question_multiple_selection_1",
//                question = "Select all the states in USA",
//                options = listOf(
//                    "Detroit",
//                    "Paris",
//                    "Detroit",
//                    "Dallas",
//                ),
//            ),
//            QuizQuestion.OpenEnded(
//                id = "question_open_ended_1",
//                question = "Describe an Android Fragment",
//                hint = "Keep it short.",
//                characterLimit = 120
//            )

        )

        return mockQuestions.shuffled().take(2)
    }
}