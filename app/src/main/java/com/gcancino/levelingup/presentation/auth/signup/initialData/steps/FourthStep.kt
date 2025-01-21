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
import com.gcancino.levelingup.presentation.auth.signup.initialData.InitialDataViewModel
import com.gcancino.levelingup.ui.components.OutlinedDropDown

@Composable
fun FourthStep(viewModel: InitialDataViewModel) {
    val options = viewModel.getObjectives()
    val selectedOption by viewModel.selectedObjective.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "What's your main goal in this moment?",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedDropDown(
                options = options,
                selectedOption = selectedOption,
                onOptionSelected = { viewModel.selectObjective(it) },
                label = ""
            )
        }

        // Button
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
        /*IconButton(
            onClick = { viewModel.nextStep() },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null
            )
        }*/
    }
}