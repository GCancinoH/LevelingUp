package com.gcancino.levelingup.data.models.patient.initialData

import java.util.Date

data class InitialBodyComposition(
    val date: Date = Date(),
    val weight: Double = 0.0,
    val bmi: Double = 0.0,
    val visceralFat: Int = 0,
    val bodyFat: Double = 0.0,
    val muscleMass: Double = 0.0,
)
