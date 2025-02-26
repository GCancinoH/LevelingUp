package com.gcancino.levelingup.domain.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gcancino.levelingup.data.models.patient.Progress
import com.gcancino.levelingup.domain.database.entities.PatientEntity
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPatient(patient: PatientEntity)

    @Query("SELECT * FROM patients WHERE uid = :uid")
    fun getPatientByUid(uid: String): Flow<PatientEntity?>

    @Query("SELECT * FROM patients")
    fun getAllPatients(): Flow<List<PatientEntity>>

    @Query("SELECT * FROM patients WHERE uid = :uid AND needs_sync = 1")
    fun getPendingSyncPlayer(uid: String): Flow<PatientEntity>

    @Query("UPDATE patients SET progress = :progress, needs_sync = 1 WHERE uid = :uid")
    fun updatePatientProgress(uid: String, progress: Progress)

    @Query("UPDATE patients SET needs_sync = 1 WHERE uid = :uid")
    fun markForSync(uid: String)

    @Query("UPDATE patients SET needs_sync = 0, last_sync = :lastSync WHERE uid = :uid")
    fun markAsSynced(uid: String, lastSync: Date = Date())
}