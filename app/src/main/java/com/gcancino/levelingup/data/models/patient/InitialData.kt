package com.gcancino.levelingup.data.models.patient

import com.gcancino.levelingup.data.models.patient.initialData.InitialBodyComposition
import com.gcancino.levelingup.data.models.patient.initialData.InitialBodyMeasures
import com.gcancino.levelingup.data.models.patient.initialData.InitialSkinFolds


data class InitialData(
    val bodyComposition: InitialBodyComposition,
    val bodyMeasurements: InitialBodyMeasures? = null,
    val bodySkinFolds: InitialSkinFolds? = null
)
