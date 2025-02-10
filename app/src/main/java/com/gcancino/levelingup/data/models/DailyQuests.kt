package com.gcancino.levelingup.data.models

import com.gcancino.levelingup.data.models.quests.QuestAttributes
import com.gcancino.levelingup.data.models.quests.QuestDetails
import com.gcancino.levelingup.data.models.quests.QuestRewards
import com.gcancino.levelingup.data.models.quests.QuestStatus
import com.gcancino.levelingup.data.models.quests.QuestType
import java.time.LocalDate

data class DailyQuest(
    val id: String,
    val qID: String,
    val types: List<QuestType> = listOf(),
    val title: String,
    val description: String,
    val status: QuestStatus = QuestStatus.NOT_STARTED,
    val date: LocalDate = LocalDate.now(),
    val rewards: QuestRewards,
    val details: QuestDetails? = null
)

/*val dailyQuests = listOf(
    DailyQuest(
        id = "dailyQuest_001",
        multipleTypes = listOf(QuestType.MENTAL_TOUGHNESS, QuestType.RECOVERY),
        title = "Cold Bath",
        description = "",
        status = QuestStatus.NOT_STARTED,
        date = LocalDate.now(),
        rewards = QuestRewards(
            xp = 10,
            coins = 5,
            attributes = QuestAttributes(health = 3, intelligence = 3)
        ),
        details = QuestDetails.RecoverQuest(
            coldBaths = 1
        )
    ),
    DailyQuest(
        id = "dailyQues_002",
        type = QuestType.RECOVERY,
        title = "Walking",
        description = "",
        status = QuestStatus.NOT_STARTED,
        date = LocalDate.now(),
        rewards = QuestRewards(
            xp = 3,
            coins = 5,
            attributes = QuestAttributes(health = 3)
        ),
        details = QuestDetails.RecoverQuest(
            walkingSteps = 6000
        )
    ),
    DailyQuest(
        id = "dailyQuest_003",
        type = QuestType.RECOVERY,
        title = "Water Consumption",
        description = "",
        status = QuestStatus.NOT_STARTED,
        date = LocalDate.now(),
        rewards = QuestRewards(
            xp = 3,
            coins = 5,
            attributes = QuestAttributes(health = 3)
        ),
        details = QuestDetails.RecoverQuest(
            waterLts = 2.5
        )
    )


)*/