package com.cricut.androidassessment.di

import com.cricut.androidassessment.data.MockQuizRepository
import com.cricut.androidassessment.data.QuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindQuizRepository(
        mockQuizRepository: MockQuizRepository,
    ): QuizRepository
}
