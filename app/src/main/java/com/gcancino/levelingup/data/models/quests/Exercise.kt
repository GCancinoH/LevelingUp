package com.gcancino.levelingup.data.models.quests

data class Exercise(
    val type: ExerciseType,
    val currentReps: Int? = 0,
    val currentSeconds: Int? = 0,
    val targetReps: Int? = 0,
    val targetSeconds: Int? = 0,
    val progressionIncrement: Int? = 5,
    val measurementType: MeasurementType? = MeasurementType.REPETITIONS
)

enum class ExerciseType {
    DEAD_HANG, DIP_HOLD, SCAPULAR_PULL_UP, HOLLOW_BODY, L_SIT, SQUATS,
    PUSH_UP, PULL_UPS, SISSY_SQUATS, LEG_RAISES, V_SIT, RUNNING, JUMP_ROPE, SWIMMING, JUMPING
}

enum class MeasurementType {
    TIME_SECONDS,
    REPETITIONS,
    DISTANCE
}
