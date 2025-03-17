package com.gcancino.levelingup.domain.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.gcancino.levelingup.domain.database.entities.DailyQuestEntity
import com.gcancino.levelingup.domain.database.entities.ExerciseProgressEntity

@Dao
interface ExerciseProgressDao {
    val questDao: DailyQuestDao

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseProgress(exerciseProgress: ExerciseProgressEntity)

    @Update
    suspend fun updateExerciseProgress(exerciseProgress: ExerciseProgressEntity)

    @Query("SELECT * FROM exercise_progress WHERE questId = :questId")
    suspend fun getExerciseProgressByQuestId(questId: String): ExerciseProgressEntity?

    @Transaction
    suspend fun insertQuestWithProgress(
        quest: DailyQuestEntity,
        progressList: List<ExerciseProgressEntity>) {
        questDao.insertQuest(quest)
        progressList.forEach {
            insertExerciseProgress(it)
        }
    }

}