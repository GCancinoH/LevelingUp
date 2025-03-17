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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.gcancino.levelingup.presentation.user.dashboard.BodyCompositionBSViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@ExperimentalMaterial3Api
@Composable
fun BodyCompositionBS(
    snackbarState: SnackbarHostState,
    viewModel: BodyCompositionBSViewModel = viewModel(),
    onDismiss: () -> Unit
) {
    val today = LocalDate.now()
    val formatedDate = today.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
    val bodyCompositionState by viewModel.bodyCompositionData.collectAsState()
    val purpleBlueGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF6650A4),
            Color(0xFF4A69BD)
        )
    )

    LaunchedEffect(key1 = bodyCompositionState) {
        when (bodyCompositionState) {
            is Resource.Success -> {
                onDismiss()
                snackbarState.showSnackbar(
                    message = "Data saved successfully",
                    withDismissAction = true
                )
            }
            is Resource.Error -> {
                onDismiss()
                snackbarState.showSnackbar(
                    message = "Error saving data",
                    withDismissAction = true
                )
            }
            else -> null
        }
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Body Composition Log",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "on $formatedDate",
            style = MaterialTheme.typography.bodySmall
        )
        /* Weight & BMI */
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()

        ) {
            OutlinedTextField(
                value = viewModel.weight,
                onValueChange = { it -> viewModel.weight = it },
                label = { Text("Weight (in kg)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = viewModel.bmi,
                onValueChange = { it -> viewModel.bmi = it },
                label = { Text("BMI") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )
        }
        /* Body Fat & Muscle Mass */
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()

        ) {
            OutlinedTextField(
                value = viewModel.bodyFat,
                onValueChange = { it -> viewModel.bodyFat = it },
                label = { Text("Body Fat (%)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = viewModel.muscleMass,
                onValueChange = { it -> viewModel.muscleMass = it },
                label = { Text("Muscle Mass (%)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )
        }
        /* Visceral Fat & Body Age */
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()

        ) {
            OutlinedTextField(
                value = viewModel.visceralFat,
                onValueChange = { it -> viewModel.visceralFat = it },
                label = { Text("Visceral Fat (%)") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = viewModel.bodyAge,
                onValueChange = { it -> viewModel.bodyAge = it },
                label = { Text("Body Age") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
        }

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
            when (bodyCompositionState) {
                is Resource.Loading -> CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                else -> Text(text = "Save my data")
            }
        }
    }
}