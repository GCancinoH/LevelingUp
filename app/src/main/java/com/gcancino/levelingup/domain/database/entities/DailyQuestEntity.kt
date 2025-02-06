package com.gcancino.levelingup.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_quests")
data class DailyQuestEntity(
    @PrimaryKey val id: String,
    val type: String,
    val title: String,
    val description: String,
    val status: String,
    val date: String,
    val rewards: String,
    val details: String,
)
