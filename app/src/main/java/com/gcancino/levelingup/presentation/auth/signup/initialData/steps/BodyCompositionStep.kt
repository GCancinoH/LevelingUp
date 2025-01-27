package com.gcancino.levelingup.presentation.auth.signup.initialData.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gcancino.levelingup.presentation.auth.signup.initialData.InitialDataViewModel

@Composable
fun BodyCompositionStep(
    viewModel: InitialDataViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Body Composition",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Body composition metrics help trac fitness progress and set realistic goals. " +
                        "Also, other health metrics can be calculated with your muscle mass, body fat " +
                        "& visceral fat.",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Body Fat TextField
            OutlinedTextField(
                value = viewModel.fatPercentage,
                onValueChange = { viewModel.onFatPercentageChange(it) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MonitorWeight,
                        contentDescription = "Body Fat"
                    )
                },
                label = { Text("Body Fat %") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Muscle Mass TextField
            OutlinedTextField(
                value = viewModel.musclePercentage,
                onValueChange = { viewModel.onMusclePercentageChange(it) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MonitorWeight,
                        contentDescription = "Muscle Mass"
                    )
                },
                label = { Text("Muscle Mass %") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Visceral Fat TextField
            OutlinedTextField(
                value = viewModel.visceralFat,
                onValueChange = { viewModel.onVisceralFatChange(it) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MonitorWeight,
                        contentDescription = "Visceral Fat"
                    )
                },
                label = { Text("Visceral Fat") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                )
            )

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