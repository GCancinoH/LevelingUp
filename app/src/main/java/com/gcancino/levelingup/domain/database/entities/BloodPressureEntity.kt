package com.gcancino.levelingup.domain.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "blood_pressure")
data class BloodPressureEntity(
    @PrimaryKey val id: String,
    val playerID: String,
    val systolic: Int,
    val diastolic: Int,
    val pulsePerMin: Int,
    val date: Date
)
