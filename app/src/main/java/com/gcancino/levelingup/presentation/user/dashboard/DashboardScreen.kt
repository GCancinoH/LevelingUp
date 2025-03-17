package com.gcancino.levelingup.presentation.user.dashboard

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

@Composable()
fun DashboardScreen(
    viewModel: DashboardViewModel
) {

    // Collect all body composition data
    val bodyCompositionData = viewModel.getBodyCompositionData().collectAsState(initial = emptyList())

    LazyColumn {
        items(bodyCompositionData.value.size) { data ->
            Button(onClick = {
                viewModel.deleteDataByID(bodyCompositionData.value[data].id)
            }) {
                Text("uuid: ${bodyCompositionData.value[data].id}")
            }


        }
    }
}