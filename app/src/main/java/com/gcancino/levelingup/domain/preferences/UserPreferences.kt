package com.gcancino.levelingup.domain.preferences

import com.gcancino.levelingup.data.models.quests.QuestType
import java.time.LocalDate

data class UserPreferences(
    val areDailyQuestsLoaded: Boolean,
    val lastQuestReset: LocalDate,
    val selectedImprovements: Set<QuestType>
)
