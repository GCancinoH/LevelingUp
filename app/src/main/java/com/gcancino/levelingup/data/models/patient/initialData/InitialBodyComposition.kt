package com.gcancino.levelingup.data.models.patient.initialData

import java.util.Date

data class InitialBodyComposition(
    val date: Date,
    val weight: Double,
    val bmi: Double,
    val visceralFat: Int,
    val bodyFat: Double,
    val muscleMass: Double,
)
