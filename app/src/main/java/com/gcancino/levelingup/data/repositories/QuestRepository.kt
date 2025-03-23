package com.gcancino.levelingup.data.repositories

import androidx.work.WorkManager
import com.gcancino.levelingup.data.models.DailyQuest
import com.gcancino.levelingup.data.models.patient.Improvement
import com.gcancino.levelingup.data.models.quests.QuestDetails
import com.gcancino.levelingup.data.models.quests.QuestRewards
import com.gcancino.levelingup.data.models.quests.QuestStatus
import com.gcancino.levelingup.data.models.quests.QuestType
import com.gcancino.levelingup.domain.database.dao.DailyQuestDao
import com.gcancino.levelingup.domain.database.dao.ExerciseProgressDao
import com.gcancino.levelingup.domain.database.entities.DailyQuestEntity
import com.gcancino.levelingup.domain.database.entities.ExerciseProgressEntity
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.domain.preferences.DataStoreManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class QuestRepository(
    private val db: FirebaseFirestore,
    private val storeManager: DataStoreManager,
    private val playerRepository: PlayerRepository,
    private val questDB: DailyQuestDao,
    private val exerciseDB: ExerciseProgressDao,
) {

    /*
        Initialize quests
    */
    suspend fun initializeQuests(quests: List<DailyQuest>): Resource<Unit> {
        return try {
            val userPreferences = storeManager.userPreferences.first()
            if (!userPreferences.areDailyQuestsLoaded) {
                val dbQuests = fetchQuestFromFirestore()
                
                when(dbQuests) {
                    is Resource.Success -> {
                        val playerImprovements = playerRepository.getPlayerImprovements()
                        val filteredQuests = dbQuests.data?.filter { quest ->
                            quest.types.any { questTypes ->
                                val improvement = convertQuestTypeToImprovement(questTypes)
                                improvement != null && playerImprovements.contains(improvement)
                            }
                        } ?: emptyList()

                        val questEntities = filteredQuests.map { quest ->
                            DailyQuestEntity(
                                id = quest.id,
                                qID = quest.qID,
                                types = quest.types,
                                title = quest.title,
                                description = quest.description,
                                status = QuestStatus.NOT_STARTED,
                                date = LocalDate.now(),
                                startedDate = quest.startedDate,
                                finishedDate = quest.finishedDate,
                                rewards = quest.rewards,
                                details = quest.details
                            )
                        }

                        questDB.insertAllQuests(questEntities)
                        storeManager.updateQuestLoadedStatus(LocalDate.now())
                        Resource.Success(Unit)
                    }
                    is Resource.Error -> Resource.Error(dbQuests.message ?: "Failed to fetch quests")
                    else -> Resource.Error("Unknown error")
                }
            } else {
                Resource.Success(Unit)
            }

        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to initialize quests: ${e.message}")
        }
    }

    /*
        Reset Daily Quest
    */
    suspend fun resetDailyQuests() {
        val userPreferences = storeManager.userPreferences.first()
        val today = LocalDate.now()

        if (userPreferences.lastQuestReset < today) {
            val quests = questDB.getAllQuestStatic()
            quests.forEach { quest ->
                questDB.updateQuest(
                    quest.copy(
                        status = QuestStatus.NOT_STARTED,
                        date = today,
                        startedDate = null,
                        finishedDate = null
                    )
                )
            }
            storeManager.updateQuestLoadedStatus(today)
        }
    }

    fun observeQuest(): Flow<List<DailyQuestEntity>> = questDB.getAllQuests()

    fun observeQuestByDate(date: LocalDate): Flow<List<DailyQuestEntity>> =
        questDB.getQuestByDate(date.toString())

    fun observeQuestByStatus(date: LocalDate): Flow<List<DailyQuestEntity>> =
        questDB.getQuestsByStatus(date.toString())

    /*
        Get data from the server
    */
    suspend fun fetchQuestFromFirestore(): Resource<List<DailyQuest>> {
        return try {
            val questSnapshot = db.collection("quests").get().await()

            val quests = questSnapshot.documents.mapNotNull { doc ->
                doc.toObject(DailyQuest::class.java)?.copy(
                    id = doc.id,
                    date = LocalDate.now()
                )
            }
            Resource.Success(quests)
        } catch (e: Exception) {
           Resource.Error(e.message ?: "Failed to fetch quests: ${e.message}")
        }
    }

    private fun convertQuestTypeToImprovement(questType: QuestType): Improvement? {
        return when (questType) {
            QuestType.STRENGTH -> Improvement.STRENGTH
            QuestType.MOBILITY -> Improvement.MOBILITY
            QuestType.MENTAL_TOUGHNESS -> Improvement.MENTAL_TOUGHNESS
            QuestType.SELF_DEVELOPMENT -> Improvement.SELF_DEVELOPMENT
            QuestType.RECOVERY -> Improvement.RECOVERY
        }
    }
}
