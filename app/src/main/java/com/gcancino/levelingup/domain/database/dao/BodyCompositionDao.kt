package com.gcancino.levelingup.domain.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gcancino.levelingup.domain.database.entities.BodyCompositionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.UUID

@Dao
interface BodyCompositionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBodyComposition(bodyComposition: BodyCompositionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBodyComposition(bodyComposition: BodyCompositionEntity)

    @Query("DELETE FROM body_composition WHERE id = :id")
    suspend fun deleteDataByID(id: UUID)

    @Query("SELECT * FROM body_composition WHERE id = :id AND playerID = :playerID")
    fun getBodyComposition(id: UUID, playerID: String): Flow<BodyCompositionEntity?>

    @Query("SELECT * FROM body_composition WHERE playerID = :playerID ORDER BY date DESC")
    fun getAllBodyCompositions(playerID: String): Flow<List<BodyCompositionEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM body_composition WHERE playerID = :playerID AND date = :date)")
    fun checkIfEntryExists(playerID: String, date: LocalDate): Flow<Boolean>

}
