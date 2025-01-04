package com.gcancino.levelingup.presentation.auth.signup.initialData

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable

import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.FirstStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.FourthStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.SecondStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.ThirdStep

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun InitialDataScreen(
    viewModel: InitialDataViewModel
) {
    AnimatedContent(
        targetState = viewModel.currentStep,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)) togetherWith
                    fadeOut(animationSpec = tween(500)) using
                    SizeTransform { initialSize, targetSize ->
                        keyframes {
                            durationMillis = 500
                            initialSize at 0 using LinearOutSlowInEasing
                            targetSize at 300 using FastOutSlowInEasing
                        }
                    }
        }
    ) { targetStep ->
        when (targetStep) {
            0 -> FirstStep(viewModel)
            1 -> SecondStep(viewModel)
            2 -> ThirdStep(viewModel)
            3 -> FourthStep(viewModel)
        }
    }
    /*when (viewModel.currentStep) {
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
        2 -> {
            AnimatedVisibility(
                visible = viewModel.currentStep == 2,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)) + slideInVertically(initialOffsetY = { -40 }, animationSpec = tween(durationMillis = 300)),
                exit = fadeOut(animationSpec = tween(durationMillis = 500)) + slideOutVertically(targetOffsetY = { -40 }, animationSpec = tween(durationMillis = 300))
            ) { ThirdStep(viewModel) }
        }
    }*/

}
