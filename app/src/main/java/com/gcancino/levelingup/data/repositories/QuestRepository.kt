package com.gcancino.levelingup.data.repositories

import android.util.Log
import com.gcancino.levelingup.data.models.DailyQuest
import com.gcancino.levelingup.data.models.patient.Improvement
import com.gcancino.levelingup.data.models.quests.QuestDetails
import com.gcancino.levelingup.data.models.quests.QuestAttributes
import com.gcancino.levelingup.data.models.quests.QuestDetails.MobilityQuestDetails
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.UUID

class QuestRepository(
    private val db: FirebaseFirestore,
    private val storeManager: DataStoreManager,
    private val playerRepository: PlayerRepository,
    private val questDB: DailyQuestDao,
    private val exerciseDB: ExerciseProgressDao,
) {

    val dailyQuests = listOf(
        DailyQuestEntity(
            id = UUID.randomUUID().toString(),
            qID = "dailyQuest_001",
            title = "MORNING_MOBILITY",
            description = "MORNING_MOBILITY_DESCRIPTION",
            types = listOf(QuestType.MOBILITY),
            date = LocalDate.now(),
            status = QuestStatus.NOT_STARTED,
            rewards = QuestRewards(
                xp = 5,
                coins = 5,
                attributes = QuestAttributes(
                    health = 2,
                    mobility = 5
                )
            ),
            details = MobilityQuestDetails(
                type = QuestType.MOBILITY.name,
                progressionIncrement = 0.1f,
                targetMinutes = 10,
                currentMinutes = 0
            )
        ),
        DailyQuestEntity(
            id = UUID.randomUUID().toString(),
            qID = "dailyQuest_002",
            title = "SLEEP",
            description = "SLEEP_DESCRIPTION",
            types = listOf(QuestType.RECOVERY),
            date = LocalDate.now(),
            status = QuestStatus.NOT_STARTED,
            rewards = QuestRewards(
                xp = 10,
                coins = 10,
                attributes = QuestAttributes(
                    health = 10,
                    intelligence = 10
                )
            ),
            details = QuestDetails.RecoveryQuestDetails(
                type = QuestType.RECOVERY.name,
                progressionIncrement = 0.0f,
                targetHours = 8f,
                currentHours = 0f
            )
        ),
        DailyQuestEntity(
            id = UUID.randomUUID().toString(),
            qID = "dailyQuest_003",
            title = "COLD_BATH",
            description = "COLD_BATH_DESCRIPTION",
            types = listOf(QuestType.RECOVERY, QuestType.MENTAL_TOUGHNESS),
            date = LocalDate.now(),
            status = QuestStatus.NOT_STARTED,
            rewards = QuestRewards(
                xp = 5,
                coins = 5,
                attributes = QuestAttributes(
                    health = 5,
                )
            ),
            details = QuestDetails.MentalToughnessQuestDetails(
                type = QuestType.MENTAL_TOUGHNESS.name,
                progressionIncrement = 0.0f,
                targetColdShowers = 1,
                currentColdShowers = 0
            )
        )
    )

    /*
        Initialize quests
    */
    suspend fun initializeQuests(): Resource<Unit> {
        return try {
            val userPreferences = storeManager.userPreferences.first()
            if (!userPreferences.areDailyQuestsLoaded) {
                val playerImprovements = playerRepository.getPlayerImprovements()
                Log.d("QuestRepository", "Player improvements: $playerImprovements")

                val filteredQuests = dailyQuests.filter { quest ->
                    quest.types?.any { questType ->
                        playerImprovements.any { it.name == questType.name }
                    } == true
                }

                withContext(Dispatchers.IO) {
                    questDB.insertAllQuests(filteredQuests)
                }

                storeManager.updateQuestLoadedStatus(LocalDate.now())
                Log.d("QuestRepository", "Quests initialized successfully")
                Resource.Success(Unit)
            } else {
                Resource.Success(Unit)
            }
        } catch(e: Exception) {
            Log.e("QuestRepository", "Failed to initialize quests: ${e.message}", e)
            Resource.Error(
                message = "Failed to initialize quests: ${e.message}",
                exception = e
            )
        }
    }

    suspend fun initializeQuestsFromFirestore(): Resource<Unit> {
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
            println(e.stackTrace)
            Resource.Error(e.stackTraceToString())

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

    fun observeQuest(): Flow<List<DailyQuestEntity>> =
        questDB.getAllQuests().flowOn(Dispatchers.IO)

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
            Log.d("QuestRepository:FetchQuestFromFirestore", "Error fetching quests: ${e.message}")
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
            QuestType.ENDURANCE -> Improvement.ENDURANCE

        }
    }
}
