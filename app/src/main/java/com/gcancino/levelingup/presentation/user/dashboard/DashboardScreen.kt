package com.gcancino.levelingup.presentation.user.dashboard


import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.gcancino.levelingup.domain.database.entities.DailyQuestEntity
import com.gcancino.levelingup.domain.entities.Resource

@Composable()
fun DashboardScreen(
    viewModel: DashboardViewModel
) {

    // Collect all body composition data
    val bodyCompositionData = viewModel.getBodyCompositionData().collectAsState(initial = emptyList())
    val questLoadingState by viewModel.questLoadingState.collectAsState()
    val dailyQuests by viewModel.dailyQuests.collectAsState(initial = emptyList())

    when (questLoadingState) {
        is Resource.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = Color.White
            )
            Text("Loading quests...")
        }
        is Resource.Error -> {
            Text("Error loading quests: ${(questLoadingState as Resource.Error).message}")
        }
        else -> {
            if (dailyQuests.isEmpty()) {
                Text("No quests available")
            } else {
                DailyQuestList(dailyQuests)
            }
        }
    }

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

@Composable
fun DailyQuestList(dailyQuests: List<DailyQuestEntity>) {
    LazyColumn {
        items(dailyQuests) { quest ->
            DailyQuestItem(quest)
        }
    }
}

@Composable
fun DailyQuestItem(quest: DailyQuestEntity) {
    // Display the quest information here
    Text(text = quest.title)
}