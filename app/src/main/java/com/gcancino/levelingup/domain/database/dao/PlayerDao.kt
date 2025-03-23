package com.gcancino.levelingup.domain.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gcancino.levelingup.data.models.patient.Improvement
import com.gcancino.levelingup.data.models.patient.Progress
import com.gcancino.levelingup.domain.database.entities.PlayerEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlayer(patient: PlayerEntity)

    @Query("SELECT * FROM players WHERE uid = :uid LIMIT 1")
    suspend fun getPlayerByID(uid: String): PlayerEntity?

    @Query("SELECT * FROM players")
    fun getAllPlayers(): Flow<List<PlayerEntity>>

    @Query("SELECT improvements from players where uid = :uid")
    fun getPlayerImprovements(uid: String): List<Improvement>

    @Query("SELECT * FROM players WHERE uid = :uid AND needs_sync = 1")
    fun getPendingSyncPlayer(uid: String): Flow<PlayerEntity>

    @Query("UPDATE players SET progress = :progress, needs_sync = 1 WHERE uid = :uid")
    fun updatePatientProgress(uid: String, progress: Progress)

    @Query("UPDATE players SET needs_sync = 1 WHERE uid = :uid")
    fun markForSync(uid: String)

    @Query("UPDATE players SET needs_sync = 0, last_sync = :lastSync WHERE uid = :uid")
    fun markAsSynced(uid: String, lastSync: Date = Date())
}