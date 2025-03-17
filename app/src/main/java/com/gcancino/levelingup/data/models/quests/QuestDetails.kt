package com.gcancino.levelingup.data.models.quests

sealed class QuestDetails {
    abstract val type: String
    abstract val progressionIncrement: Float

    data class EnduranceQuestDetails(
        override val type: String,
        override val progressionIncrement: Float,
        val targetTime: Int? = 0,
        val currentTime: Int? = 0,
        val targetDistance: Int? = 0,
        val currentDistance: Int? = 0
    ) : QuestDetails()

    data class RecoveryQuestDetails(
        override val type: String,
        override val progressionIncrement: Float,
        val targetLiters: Float? = null,
        val currentLiters: Float? = null,
        val targetHours: Float? = null,
        val currentHours: Float? = null,
        val targetSteps: Int? = null,
        val currentSteps: Int? = null,
    ) : QuestDetails()

    data class MobilityQuestDetails(
        override val type: String,
        override val progressionIncrement: Float,
        val targetMinutes: Int? = 0,
        val currentMinutes: Int? = 0,
    ) : QuestDetails()

    data class MentalToughnessQuestDetails(
        override val type: String,
        override val progressionIncrement: Float,
        val targetMinutes: Int? = 0,
        val currentMinutes: Int? = 0,
        val targetColdShowers: Int? = 0,
        val currentColdShowers: Int? = 0,
    ) : QuestDetails()

    data class StrengthQuestDetails(
        override val type: String,
        override val progressionIncrement: Float,
        val progression: StrengthProgression
    ) : QuestDetails()


    /*data class StrengthQuest(
        val progression: StrengthProgression
    ) : QuestDetails()

    data class RecoverQuest(
        val coldBaths: String? = null,
        val walkingSteps: Int? = 0,
        val waterLts: Double? = 0.0,
        val sleepHours: Int? = 0
    ) : QuestDetails()*/
}