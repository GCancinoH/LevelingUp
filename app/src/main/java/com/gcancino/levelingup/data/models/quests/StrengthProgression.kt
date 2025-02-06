package com.gcancino.levelingup.data.models.quests

import java.time.LocalDate

data class StrengthProgression(
    val currentState: Int = 1,
    val stageRequirements: List<StageRequirement>,
    val nextTestData: LocalDate? = null
)

data class StageRequirement(
    val stageNumber: Int,
    val exercises: List<ExerciseProgress>
)
