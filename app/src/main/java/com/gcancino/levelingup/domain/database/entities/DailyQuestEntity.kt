package com.gcancino.levelingup.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.gcancino.levelingup.data.models.quests.QuestDetails
import com.gcancino.levelingup.data.models.quests.QuestRewards
import com.gcancino.levelingup.data.models.quests.QuestStatus
import com.gcancino.levelingup.data.models.quests.QuestType
import com.gcancino.levelingup.domain.database.converters.Converters
import java.time.LocalDate

@Entity(tableName = "daily_quests")
data class DailyQuestEntity(
    @PrimaryKey val id: String,
    val qID: String,
    @TypeConverters(Converters::class)
    val types: List<QuestType>?,
    val title: String,
    val description: String,
    @TypeConverters(Converters::class)
    val status: QuestStatus = QuestStatus.NOT_STARTED,
    val date: LocalDate,
    val startedDate: LocalDate? = null,
    val finishedDate: LocalDate? = null,
    @TypeConverters(Converters::class)
    val rewards: QuestRewards? = null,
    @TypeConverters(Converters::class)
    val details: QuestDetails? = null,
)
