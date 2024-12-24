package com.gcancino.levelingup.presentation.auth.signup.initialData

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable

import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.FirstStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.SecondStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.ThirdStep

@Composable
fun InitialDataScreen(
    viewModel: InitialDataViewModel
) {
    when (viewModel.currentStep) {
        0 -> {
            AnimatedVisibility(
                visible = viewModel.currentStep == 0,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)) + slideInVertically(initialOffsetY = { -40 }, animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 500)) + slideOutVertically(targetOffsetY = { -40 }, animationSpec = tween(durationMillis = 300))
            ) { FirstStep(viewModel) }
        }
        1 -> {
            AnimatedVisibility(
                visible = viewModel.currentStep == 1,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)) + slideInVertically(initialOffsetY = { -40 }, animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 500)) + slideOutVertically(targetOffsetY = { -40 }, animationSpec = tween(durationMillis = 300))
            ) { SecondStep(viewModel) }
        }
    }

}
