package com.gcancino.levelingup.data.repositories

import android.content.Context
import com.gcancino.levelingup.data.models.DailyQuest
import com.gcancino.levelingup.data.models.quests.QuestDetails
import com.gcancino.levelingup.data.models.quests.QuestRewards
import com.gcancino.levelingup.data.models.quests.QuestStatus
import com.gcancino.levelingup.data.models.quests.QuestType
import com.gcancino.levelingup.domain.database.AppDatabase
import com.gcancino.levelingup.domain.database.dao.QuestDao
import com.gcancino.levelingup.domain.database.entities.DailyQuestEntity
import com.gcancino.levelingup.domain.preferences.DataStoreManager
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class QuestRepository(
    context: Context,
    private val dataStoreManager: DataStoreManager
) {

}

