package com.gcancino.levelingup.domain.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gcancino.levelingup.domain.database.entities.DailyQuestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuest(quest: DailyQuestEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuest(quests: List<DailyQuestEntity>)

    @Update
    suspend fun updateQuest(quest: DailyQuestEntity)

    @Delete
    suspend fun deleteQuest(quest: DailyQuestEntity)

    @Query("SELECT * FROM daily_quests WHERE date = :date")
    fun getQuestsByDate(date: String): Flow<List<DailyQuestEntity>>

    @Query("SELECT * FROM daily_quests")
    suspend fun getAllQuests(): Flow<List<DailyQuestEntity>>

    @Query("SELECT * FROM daily_quests WHERE id = :questID")
    suspend fun getQuestByID(questID: String): DailyQuestEntity?

    @Query("SELECT * FROM daily_quests WHERE type = :questType")
    suspend fun getQuestsByType(questType: String): Flow<List<DailyQuestEntity>>

    @Query("SELECT * FROM daily_quests WHERE status = :questStatus")
    suspend fun getQuestsByStatus(questStatus: String): Flow<List<DailyQuestEntity>>
}