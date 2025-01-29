package com.gcancino.levelingup.data.models

import com.gcancino.levelingup.data.models.quests.QuestDetails
import com.gcancino.levelingup.data.models.quests.QuestRewards
import com.gcancino.levelingup.data.models.quests.QuestStatus

data class DailyQuests(
    val questID: String,
    val name: String,
    val description: String,
    val rewards: QuestRewards,
    val status: QuestStatus = QuestStatus.NOT_STARTED,
    val details: QuestDetails
)
