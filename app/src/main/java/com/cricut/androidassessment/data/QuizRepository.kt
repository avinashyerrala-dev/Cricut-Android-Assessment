package com.cricut.androidassessment.data

import com.cricut.androidassessment.model.QuizQuestion

interface QuizRepository {
    suspend fun getQuestions(): List<QuizQuestion>
}