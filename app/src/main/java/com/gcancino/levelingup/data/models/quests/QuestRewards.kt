package com.gcancino.levelingup.data.models.quests

data class QuestRewards(
    val xp: Int,
    val coins: Int? = null,
    val attributes: QuestAttributes? = null,
)
