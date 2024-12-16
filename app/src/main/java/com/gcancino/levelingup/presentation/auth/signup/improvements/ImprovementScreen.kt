package com.gcancino.levelingup.presentation.auth.signup.improvements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gcancino.levelingup.data.models.patient.Improvement
import com.gcancino.levelingup.ui.components.TappableCard

@Composable
fun ImprovementScreen(
    viewModel: ImprovementViewModel
) {
    val improvements = Improvement.entries

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "What areas of your life would you like to improve?",
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        improvements.forEach { improvement ->
            TappableCard(
                text = when (improvement.name) {
                    "STRENGTH" -> "Strength"
                    "ENDURANCE" -> "Endurance"
                    "MOBILITY" -> "Mobility"
                    "SELF_DEVELOPMENT" -> "Self-Development"
                    "NUTRITION" -> "Nutrition"
                    "HABITS" -> "Habit Formation"
                    else -> null
                }.toString(),
                isSelected = viewModel.selectedImprovements.contains(improvement),
                onClick = { viewModel.toggleImprovement(improvement) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Row(
            modifier =  Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { viewModel.saveImprovementsToFirestore() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }

}