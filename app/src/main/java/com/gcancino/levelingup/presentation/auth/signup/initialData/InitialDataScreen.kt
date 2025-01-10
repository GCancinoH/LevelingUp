package com.gcancino.levelingup.presentation.auth.signup.initialData

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.EightStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.FirstStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.FourthStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.SecondStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.ThirdStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.FifthStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.SixStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.SeventhStep
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun InitialDataScreen(
    viewModel: InitialDataViewModel,
    context: Context
) {
    val animatedProgress by animateFloatAsState(
        targetValue = viewModel.progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = ""
    )

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFF4A69BD),
            trackColor = Color.White,

            )
    }
    Spacer(modifier = Modifier.height(32.dp))
    Box(modifier = Modifier.padding(16.dp).fillMaxSize()) {
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
            }, label = ""
        ) { targetStep ->
            when (targetStep) {
                0 -> FirstStep(viewModel)
                1 -> SecondStep(viewModel)
                2 -> ThirdStep(viewModel)
                3 -> FourthStep(viewModel)
                4 -> FifthStep(viewModel)
                5 -> SixStep(viewModel)
                6 -> SeventhStep(viewModel)
                7 -> EightStep(viewModel, context)
            }
        }
    }
}
