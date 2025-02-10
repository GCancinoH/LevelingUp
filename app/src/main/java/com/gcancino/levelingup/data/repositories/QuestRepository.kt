package com.gcancino.levelingup.data.repositories

import com.gcancino.levelingup.data.models.DailyQuest
import com.gcancino.levelingup.data.models.quests.QuestRewards
import com.gcancino.levelingup.data.models.quests.QuestStatus
import com.gcancino.levelingup.data.models.quests.QuestType
import com.gcancino.levelingup.domain.database.dao.QuestDao
import com.gcancino.levelingup.domain.database.entities.DailyQuestEntity
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.domain.preferences.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDate

class QuestRepository(
    private val questDao: QuestDao,
    private val dataStoreManager: DataStoreManager
) {
    val patientRepository = PatientRepository()

    fun observeQuest(): Flow<List<DailyQuestEntity>> =
        questDao.getAllQuests()
            .flowOn(Dispatchers.IO)

    fun observeQuestByDate(date: LocalDate): Flow<List<DailyQuestEntity>> =
        questDao.getQuestsByDate(date.toString())
            .flowOn(Dispatchers.IO)

    fun initializeDailyQuest(predefinedQuests: List<DailyQuest>): Flow<Resource<Unit>> = flow {
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
    }.flowOn(Dispatchers.IO)

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
}
