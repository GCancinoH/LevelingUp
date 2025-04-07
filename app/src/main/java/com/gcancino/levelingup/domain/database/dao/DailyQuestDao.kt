package com.gcancino.levelingup.domain.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gcancino.levelingup.domain.database.entities.DailyQuestEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

@Dao
interface DailyQuestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuest(quest: DailyQuestEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuests(quests: List<DailyQuestEntity>)

    @Update
    suspend fun updateQuest(quest: DailyQuestEntity)

    @Delete
    suspend fun deleteQuest(quest: DailyQuestEntity)

    @Query("SELECT * FROM daily_quests WHERE date = :date AND status = 'NOT_STARTED'")
    fun getQuestsByStatus(date: String): Flow<List<DailyQuestEntity>>

    @Query("SELECT * FROM daily_quests WHERE date = :date")
    fun getQuestByDate(date: String): Flow<List<DailyQuestEntity>>

    @Query("SELECT * FROM daily_quests")
    fun getAllQuests(): Flow<List<DailyQuestEntity>>

    @Query("SELECT * FROM daily_quests")
    suspend fun getAllQuestStatic(): List<DailyQuestEntity>
}