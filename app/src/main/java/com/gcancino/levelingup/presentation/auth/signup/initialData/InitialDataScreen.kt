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
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.BodyCompositionStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.ImprovementStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.ObjectivesStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.PersonalInfoStep
import com.gcancino.levelingup.presentation.auth.signup.initialData.steps.PhysicalAttributesStep
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
    Column(
        modifier = Modifier.fillMaxSize().
            padding(16.dp)
    ) {
        // Progress bar
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp),
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
        Box(modifier = Modifier.fillMaxSize()) {
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
                    0 -> PersonalInfoStep(viewModel)
                    1 -> PhysicalAttributesStep(viewModel)
                    2 -> BodyCompositionStep(viewModel)
                    3 -> ObjectivesStep(viewModel)
                    4 -> ImprovementStep(viewModel)
                }
            }
        }
    }
}