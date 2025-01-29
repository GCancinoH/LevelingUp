package com.gcancino.levelingup.data.models.quests

sealed class QuestDetails {
    data class ExerciseQuest(val exercise: List<Any>)
    data class RecoveryQuest(val duration: Int)
}
