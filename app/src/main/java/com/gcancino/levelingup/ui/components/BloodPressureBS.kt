package com.gcancino.levelingup.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.presentation.user.dashboard.BloodPressureViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun BloodPressureBS(
    viewModel: BloodPressureViewModel = viewModel()
) {
    val today = LocalDate.now()
    val formatedDate = today.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
    val bloodPressureState by viewModel.bloodPressureData.collectAsState()
    val purpleBlueGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF6650A4),
            Color(0xFF4A69BD)
        )
    )

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Blood Pressure Log",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "on $formatedDate",
            style = MaterialTheme.typography.bodySmall
        )
        /* Weight & BMI */
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = viewModel.systolic,
            onValueChange = { it -> viewModel.systolic = it },
            label = { Text("Systolic") },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            )
        )
        OutlinedTextField(
            value = viewModel.diastolic,
            onValueChange = { it -> viewModel.diastolic = it },
            label = { Text("Diastolic") },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )
        OutlinedTextField(
            value = viewModel.pulsePerMin,
            onValueChange = { it -> viewModel.pulsePerMin = it },
            label = { Text("Pulses per minute") },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { viewModel.saveDataLocally() },
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(brush = purpleBlueGradient ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            )
        ) {
            when (bloodPressureState) {
                is Resource.Loading -> CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                else -> Text(text = "Save my data")
            }
        }
    }
}