package com.gcancino.levelingup.presentation.auth.signup.initialData.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gcancino.levelingup.data.models.patient.Objectives
import com.gcancino.levelingup.presentation.auth.signup.initialData.InitialDataViewModel
import com.gcancino.levelingup.ui.components.TappableCard

@Composable
fun ObjectivesStep(
    viewModel: InitialDataViewModel
) {
    val objectives = Objectives.entries
    val selectedObjective by viewModel.selectedObjective.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Objectives",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Choose your fitness goals to customize your plan and focus on achieving your desired results.",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            objectives.forEach { objective ->
                TappableCard(
                    text = when (objective.name) {
                        "MAINTAIN_WEIGHT" -> "Maintain Weight"
                        "LOSE_FAT" -> "Lose Body Fat"
                        "INCREASE_MUSCLE" -> "Increase Muscle Mass"
                        "BODY_RECOMPOSITION" -> "Body Recomposition"
                        else -> null
                    }.toString(),
                    isSelected = selectedObjective == objective.name,
                    onClick = { viewModel.selectObjective(objective.name) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Button(
            onClick = { viewModel.nextStep() },
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomEnd),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(text = "Next")
        }
    }
}