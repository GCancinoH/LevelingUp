package com.gcancino.levelingup.utils

import com.gcancino.levelingup.data.repositories.BloodPressureData
import com.gcancino.levelingup.data.repositories.BodyCompositionData
import com.gcancino.levelingup.domain.database.entities.BloodPressureEntity
import com.gcancino.levelingup.domain.database.entities.BodyCompositionEntity

fun BodyCompositionEntity.toDomainModel(): BodyCompositionData {
    return BodyCompositionData(
        id = this.id,
        playerID = this.playerID,
        weight = this.weight,
        bmi = this.bmi,
        bodyFat = this.bodyFat,
        muscleMass = this.muscleMass,
        visceralFat = this.visceralFat,
        bodyAge = this.bodyAge,
        date = this.date
    )
}

fun BodyCompositionData.toEntityModel(): BodyCompositionEntity {
    return BodyCompositionEntity(
        id = this.id,
        playerID = this.playerID,
        weight = this.weight,
        bmi = this.bmi,
        bodyFat = this.bodyFat,
        muscleMass = this.muscleMass,
        visceralFat = this.visceralFat,
        bodyAge = this.bodyAge,
        date = this.date
    )
}

fun BloodPressureData.toEntityModel(): BloodPressureEntity {
    return BloodPressureEntity(
        id = this.id,
        playerID = this.playerID,
        systolic = this.systolic,
        diastolic = this.diastolic,
        pulsePerMin = this.pulsePerMin,
        date = this.date
    )
}

fun BloodPressureEntity.toDomainModel(): BloodPressureData {
    return BloodPressureData(
        id = this.id,
        playerID = this.playerID,
        systolic = this.systolic,
        diastolic = this.diastolic,
        pulsePerMin = this.pulsePerMin,
        date = this.date
    )
}