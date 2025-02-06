package com.gcancino.levelingup.data.models.quests

sealed class QuestDetails {
    data class StrengthQuest(
        val progression: StrengthProgression
    ) : QuestDetails()

    data class RecoverQuest(
        val coldBaths: Int? = 0,
        val walkingSteps: Int? = 0,
        val waterLts: Double? = 0.0,
        val sleepHours: Int? = 0
    ) : QuestDetails()
}