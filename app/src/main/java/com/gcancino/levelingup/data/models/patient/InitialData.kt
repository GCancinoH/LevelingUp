package com.gcancino.levelingup.data.models.patient

import com.gcancino.levelingup.data.models.patient.initialData.InitialBodyComposition
import com.gcancino.levelingup.data.models.patient.initialData.InitialBodyMeasures
import com.gcancino.levelingup.data.models.patient.initialData.InitialSkinFolds
import java.util.Date


data class InitialData(
    /*val bodyComposition: InitialBodyComposition? = null,
    val bodyMeasurements: InitialBodyMeasures? = null,
    val bodySkinFolds: InitialSkinFolds? = null*/
    val bmi: Double = 0.0,
    val date: Date = Date(),
    val fatPercentage: Double = 0.0,
    val musclePercentage: Double = 0.0,
    val photos: List<String>? = listOf(),
    val visceralFat: Int = 0,
    val weight: Double = 0.0
)
