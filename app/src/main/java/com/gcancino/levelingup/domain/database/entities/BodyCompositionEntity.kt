package com.gcancino.levelingup.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "body_composition")
data class BodyCompositionEntity(
    @PrimaryKey val id: UUID,
    val playerID: String,
    val weight: Double,
    val bmi: Double,
    val bodyFat: Double,
    val muscleMass: Double,
    val visceralFat: Int,
    val bodyAge: Int,
    val date: LocalDate
)
