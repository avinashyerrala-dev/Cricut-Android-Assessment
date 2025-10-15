package com.cricut.androidassessment.ui.screens.assessment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cricut.androidassessment.R
import com.cricut.androidassessment.model.QuizAnswer
import com.cricut.androidassessment.model.QuizQuestion
import com.cricut.androidassessment.ui.AssessmentUiState
import com.cricut.androidassessment.ui.screens.SingleSelectionOption

@Composable
fun QuestionContent(
    uiState: AssessmentUiState, modifier: Modifier, onNext: () -> Unit,
    onPrevious: () -> Unit,
    onTrueFalseAnswered: (QuizQuestion.TrueOrFalse, Boolean) -> Unit,
    onSingleChoiceAnswered: (QuizQuestion.SingleChoice, Int) -> Unit,
) {

    val question = uiState.currentQuestion ?: return
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Question(
                prompt = question.question,
            )
            when (question) {
                is QuizQuestion.TrueOrFalse -> {
                    val currentAnswer = uiState.answers[question.id] as? QuizAnswer.BooleanAnswer

                    TrueOrFalseQuestion(
                        modifier = modifier,
                        question = question,
                        selectedAnswer = currentAnswer?.value,
                        onOptionSelected = onTrueFalseAnswered
                    )
                }

                is QuizQuestion.SingleChoice -> {
                    val currentAnswer =
                        uiState.answers[question.id] as? QuizAnswer.SingleChoiceAnswer
                    SingleChoiceQuestion(
                        question = question,
                        selectedIndex = currentAnswer?.index,
                        onOptionSelected = onSingleChoiceAnswered,
                    )
                }

//                is QuizQuestion.MultipleChoice -> {
//                    MultipleChoiceQuestion()
//                }
//
//                is QuizQuestion.OpenEnded -> {
//                    OPenEndedQuestion()
//                }
            }
        }
        Spacer(modifier = modifier.height(24.dp))
        QuestionBottomNavigation(
            modifier = modifier,
            canGoBack = !uiState.isAtBeginning,
            canGoForward = !uiState.isAtEnd,
            onPrevious = onPrevious,
            onNext = onNext,
        )

    }

}

@Composable
private fun Question(
    prompt: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = prompt,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
        )
        HorizontalDivider()
    }
}

@Composable
fun QuestionBottomNavigation(
    canGoBack: Boolean,
    canGoForward: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(
            onClick = onPrevious,
            enabled = canGoBack,
        ) {
            Text(text = stringResource(R.string.previous))
        }
        Button(
            onClick = onNext,
            enabled = canGoForward,
        ) {
            Text(text = stringResource(R.string.next))
        }
    }
}

@Composable
fun SingleChoiceQuestion(
    question: QuizQuestion.SingleChoice,
    selectedIndex: Int?,
    onOptionSelected: (QuizQuestion.SingleChoice, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        question.options.forEachIndexed { index, option ->
            SingleSelectionOption(
                label = option,
                selected = selectedIndex == index,
                onClick = { onOptionSelected(question, index) },
            )
        }
    }
}

@Composable
fun TrueOrFalseQuestion(
    modifier: Modifier,
    question: QuizQuestion.TrueOrFalse,
    selectedAnswer: Boolean?,
    onOptionSelected: (QuizQuestion.TrueOrFalse, Boolean) -> Unit,

    ) {
    val options =
        listOf(
            true to stringResource(R.string.choice_true),
            false to stringResource(R.string.choice_false)
        )
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { (value, label) ->
            SingleSelectionOption(
                label = label,
                selected = selectedAnswer == value,
                onClick = { onOptionSelected(question, value) },
            )
        }
    }

}
