package com.gcancino.levelingup.data.models

import com.gcancino.levelingup.data.models.quests.QuestAttributes
import com.gcancino.levelingup.data.models.quests.QuestDetails
import com.gcancino.levelingup.data.models.quests.QuestRewards
import com.gcancino.levelingup.data.models.quests.QuestStatus
import com.gcancino.levelingup.data.models.quests.QuestType
import java.time.LocalDate

data class DailyQuest(
    val id: String,
    val qID: String,
    val types: List<QuestType> = listOf(),
    val title: String,
    val description: String,
    val status: QuestStatus = QuestStatus.NOT_STARTED,
    val date: LocalDate = LocalDate.now(),
    val startedDate: LocalDate? = null,
    val finishedDate: LocalDate? = null,
    val rewards: QuestRewards,
    val details: QuestDetails? = null
)