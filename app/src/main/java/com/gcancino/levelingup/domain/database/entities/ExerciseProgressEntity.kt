package com.gcancino.levelingup.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_progress")
data class ExerciseProgressEntity(
    @PrimaryKey val id: String,
    val questId: String,
    val type: String,
    val currentReps: Int,
    val currentSeconds: Int,
    val targetReps: Int,
    val targetSeconds: Int,
)
