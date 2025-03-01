package com.gcancino.levelingup.domain.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gcancino.levelingup.domain.database.entities.BloodPressureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BloodPressureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBloodPressure(bloodPressure: BloodPressureEntity)

    @Query("DELETE FROM blood_pressure WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM blood_pressure WHERE id = :id AND playerID = :playerID")
    fun getBloodPressureDataByID(id: String, playerID: String): Flow<BloodPressureEntity?>

    @Query("SELECT * FROM blood_pressure WHERE playerID = :playerID ORDER BY date DESC")
    fun getAllBloodPressureData(playerID: String): Flow<List<BloodPressureEntity>>
}