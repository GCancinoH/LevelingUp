package com.gcancino.levelingup.data.repositories

import androidx.work.WorkManager
import com.gcancino.levelingup.data.models.DailyQuest
import com.gcancino.levelingup.data.models.quests.QuestRewards
import com.gcancino.levelingup.data.models.quests.QuestStatus
import com.gcancino.levelingup.data.models.quests.QuestType
import com.gcancino.levelingup.domain.database.dao.PatientDao
import com.gcancino.levelingup.domain.database.dao.QuestDao
import com.gcancino.levelingup.domain.database.entities.DailyQuestEntity
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.domain.preferences.DataStoreManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class QuestRepository {}
/*class QuestRepository(
    private val questDao: QuestDao,
    private val dataStoreManager: DataStoreManager,
    private val patientDao: PatientDao,
    private val scope: CoroutineScope,
    private val workManager: WorkManager
) {
    val patientRepository = PatientRepository(
        patientDao = patientDao,
        scope = scope,
        workManager = workManager
    )
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val dispatcher = Dispatchers.IO

    fun observeQuest(): Flow<List<DailyQuestEntity>> =
        questDao.getAllQuests()
            .flowOn(dispatcher)

    fun observeQuestByDate(date: LocalDate): Flow<List<DailyQuestEntity>> =
        questDao.getQuestsByDate(date.toString())
            .flowOn(dispatcher)

    fun fetchQuestFromFirestore(): Flow<Resource<List<DailyQuest>>> = flow {
        emit(Resource.Loading())
        try {
            val questsSnapshot = db.collection("quests").get().await()
            val quests = questsSnapshot.documents.mapNotNull { doc ->
                doc.toObject(DailyQuest::class.java)?.copy(id = doc.id)
            }
            emit(Resource.Success(quests))
        } catch(e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error occurred"))
        }
    }.flowOn(dispatcher)

    fun initializedDailyQuest(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val userPreferences = dataStoreManager.userPreferences.first()
            if(!userPreferences.areDailyQuestsLoaded) {
                val firestoreQuests = fetchQuestFromFirestore()
                    .first { it !is Resource.Loading }

                when (firestoreQuests) {
                    is Resource.Success -> {
                        val userImprovements = patientRepository.getUserImprovements().first()
                        val filteredQuests = firestoreQuests.data?.filter { quest ->
                            quest.types.any { userImprovements.contains(it) }
                        } ?: emptyList()

                        val questEntities = filteredQuests.map { quest ->
                            DailyQuestEntity(
                                id = quest.id,
                                type = quest.types,
                                title = quest.title,
                                description = quest.description,
                                status = QuestStatus.NOT_STARTED,
                                date = LocalDate.now(),
                                rewards = quest.rewards,
                                details = quest.details
                            )
                        }

                        questDao.insertAllQuest(questEntities)
                        dataStoreManager.updateQuestLoadedStatus(LocalDate.now())
                        emit(Resource.Success(Unit))
                    }
                    is Resource.Error -> emit(Resource.Error(firestoreQuests.message ?: "Unknown error occurred"))
                    else -> emit(Resource.Error("Unexpected state"))
                }
            } else {
                emit(Resource.Success(Unit))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error occurred"))
        }
    }.flowOn(dispatcher)
    /*fun initializeDailyQuest(predefinedQuests: List<DailyQuest>): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val userPreferences = dataStoreManager.userPreferences.first()

            if(!userPreferences.areDailyQuestsLoaded) {
                val userImprovements = patientRepository.getUserImprovements().first()

                val filteredQuests = predefinedQuests.filter { quest ->
                    quest.type?.let { userImprovements.contains(it) } ?:
                    quest.multipleTypes?.any { userImprovements.contains(it) } ?: false
                }

                val questEntities = filteredQuests.map { quest ->
                    DailyQuestEntity(
                        id = quest.id,
                        type = quest.type,
                        multipleTypes = quest.multipleTypes,
                        title = quest.title,
                        description = quest.description,
                        status = quest.status,
                        date = quest.date,
                        rewards = quest.rewards,
                        details = quest.details
                    )
                }
                questDao.insertAllQuest(questEntities)
                dataStoreManager.updateQuestLoadedStatus(LocalDate.now())
                emit(Resource.Success(Unit))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error occurred"))
        }
    }.flowOn(Dispatchers.IO)*/

    fun resetDailyQuest(): Flow<Unit> = flow {
        dataStoreManager.userPreferences
            .first()
            .let { prefs ->
                val today = LocalDate.now()
                if (prefs.lastQuestReset < today) {
                    observeQuest().first().map { quest ->
                        quest.copy(
                            status = QuestStatus.NOT_STARTED,
                            date = today
                        )
                    }.forEach { quest ->
                        questDao.updateQuest(quest)
                    }
                    dataStoreManager.updateQuestLoadedStatus(today)
                    emit(Unit)
                }
            }
    }.flowOn(Dispatchers.IO)

    fun updateQuestStatus(questID: String, status: QuestStatus): Flow<Unit> = flow {
        questDao.getQuestByID(questID)?.let { quest ->
            questDao.updateQuest(quest.copy(status = status))
            if (status == QuestStatus.COMPLETED) {
                applyQuestRewards(quest.rewards)
            }
            emit(Unit)
        }
    }.flowOn(Dispatchers.IO)

    private fun applyQuestRewards(rewards: QuestRewards): Flow<Unit> = flow {
        emit(Unit)
    }.flowOn(Dispatchers.IO)

    fun getQuestsByType(questType: QuestType): Flow<List<DailyQuestEntity>> =
        questDao.getQuestsByType(questType.name)
            .flowOn(Dispatchers.IO)

    fun getQuestsByStatus(questStatus: QuestStatus): Flow<List<DailyQuestEntity>> =
        questDao.getQuestsByStatus(questStatus.name)
            .flowOn(Dispatchers.IO)
}*/