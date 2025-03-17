package com.gcancino.levelingup.data.models.quests

import com.gcancino.levelingup.data.models.DailyQuest

data class ExerciseProgress(
    val exerciseID: String,
    val type: ExerciseType,
    val currentReps: Int = 0,
    val currentSeconds: Int = 0,
    val targetReps: Int = 0,
    val targetSeconds: Int = 0,
    val progressionIncrement: Int = 5
)

enum class ExerciseType {
    DEAD_HANG, DIP_HOLD, SCAPULAR_PULL_UP, HOLLOW_BODY, L_SIT, SQUATS, RUNNING,
    JUMP_ROPE, JUMPING, SWIMMING, PUSH_UP, PULL_UPS, SISSY_SQUATS, LEG_RAISES, V_SIT
}