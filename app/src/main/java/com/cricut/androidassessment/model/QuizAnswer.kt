package com.cricut.androidassessment.model

sealed interface QuizAnswer {
    data class BooleanAnswer(val value: Boolean) : QuizAnswer
    data class SingleChoiceAnswer(val index: Int) : QuizAnswer
//    data class MultipleSelectionAnswer(val indices: Set<Int>) : QuizAnswer
//    data class TextAnswer(val value: String) : QuizAnswer
}
