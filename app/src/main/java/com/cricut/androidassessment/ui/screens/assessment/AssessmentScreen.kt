package com.cricut.androidassessment.ui.screens.assessment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cricut.androidassessment.R
import com.cricut.androidassessment.model.QuizAnswer
import com.cricut.androidassessment.ui.AssessmentViewModel
import com.cricut.androidassessment.ui.theme.AndroidAssessmentTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessmentScreen(
    modifier: Modifier = Modifier,
    viewModel: AssessmentViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = stringResource(R.string.cricut_quiz), style = MaterialTheme.typography.titleLarge) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
            )
        )

    }, content = { paddingValues ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background,
        ) {
            when {
                uiState.isLoading -> LoadingState()
                uiState.errorMessage != null -> TODO("Show an error UI state")
                else -> {
                    QuestionContent(
                        uiState = uiState,
                        modifier = modifier,
                        onNext = viewModel::goToNextQuestion,
                        onPrevious = viewModel::goToPreviousQuestion,
                        onTrueFalseAnswered = { question, answer ->
                            viewModel.recordAnswer(
                                questionId = question.id,
                                answer = QuizAnswer.BooleanAnswer(answer)
                            )
                        },
                        onSingleChoiceAnswered = { question, answerIndex ->
                            viewModel.recordAnswer(
                                questionId = question.id,
                                answer = QuizAnswer.SingleChoiceAnswer(answerIndex),
                            )
                        }
                    )
                }

            }

        }

    })

}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Loading questions...")
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewAssessmentScreen() {
    AndroidAssessmentTheme {
        AssessmentScreen()
    }
}
