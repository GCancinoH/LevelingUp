package com.gcancino.levelingup.data.models.quests

sealed class QuestDetails {
    data class StrengthQuest(
        val stage: Int = 1,
        val exercises: List<Exercise>,
        val stageRequirements: Map<Int, List<Exercise>> = mapOf(
            1 to listOf(
                Exercise(type = ExerciseType.DEAD_HANG, targetSeconds = 180),
                Exercise(type = ExerciseType.DIP_HOLD, targetSeconds = 120),
                Exercise(type = ExerciseType.SCAPULAR_PULL_UP, targetReps = 25),
                Exercise(type = ExerciseType.HOLLOW_BODY, targetSeconds = 120),
                Exercise(type = ExerciseType.L_SIT, targetSeconds = 90),
                Exercise(type = ExerciseType.SQUATS, targetReps = 100),
            ),
            2 to listOf(
                Exercise(type = ExerciseType.PUSH_UP, targetReps = 100),
                Exercise(type = ExerciseType.PULL_UPS, targetReps = 100),
                Exercise(type = ExerciseType.SISSY_SQUATS, targetReps = 100),
                Exercise(type = ExerciseType.LEG_RAISES, targetReps = 100),
                Exercise(type = ExerciseType.V_SIT, targetReps = 100),
            )
        )
    ) : QuestDetails()

    data class EnduranceQuest(
        val activities: List<Exercise>,
        val progressionRate: Float = 0.05f
    )

    data class MobilityQuest(
        val routine: String,
        val currentMinutes: Int,
        val targetMinutes: Int,
        val progressionIncrement: Int = 2
    )

    data class MentalToughnessQuest(
        val coldShowerPerWeek: Int,
        val fastingDays: List<DayOfWeek>,
        val currentColdShowerTemp: Int = 20
    )

}
