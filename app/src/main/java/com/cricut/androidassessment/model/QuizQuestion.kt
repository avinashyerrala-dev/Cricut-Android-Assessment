package com.cricut.androidassessment.model

sealed class QuizQuestion(open val id: String, open val question: String) {
    data class TrueOrFalse(override val id: String, override val question: String) :
        QuizQuestion(id, question)

    data class SingleChoice(
        override val id: String,
        override val question: String,
        val options: List<String>
    ) : QuizQuestion(id, question)

//    data class MultipleChoice(
//        override val id: String,
//        override val question: String,
//        val options: List<String>
//    ) :
//        QuizQuestion(id, question)
//
//    data class OpenEnded(
//        override val id: String,
//        override val question: String,
//        val hint: String,
//        val characterLimit: Int
//    ) : QuizQuestion(id, question)

}