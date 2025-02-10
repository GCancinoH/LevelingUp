package com.gcancino.levelingup.data.models.quests

sealed class QuestDetails {
    data class StrengthQuest(
        val progression: StrengthProgression
    ) : QuestDetails()

    data class RecoverQuest(
        val coldBaths: String? = null,
        val walkingSteps: Int? = 0,
        val waterLts: Double? = 0.0,
        val sleepHours: Int? = 0
    ) : QuestDetails()
}