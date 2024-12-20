package com.gcancino.levelingup.domain.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gcancino.levelingup.data.models.patient.Progress
import com.gcancino.levelingup.domain.database.entities.Patient
import kotlinx.coroutines.flow.Flow

interface PatientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: Patient)

    @Query("SELECT * FROM patients WHERE uid = :uid")
    fun getPatient(uid: String): Flow<Patient?>

    @Query("SELECT :parameter FROM patients WHERE uid = :uid")
    fun getParameter(uid: String, parameter: String): Flow<String?>

    @Query("UPDATE patients SET progress = :progress WHERE uid = :uid")
    suspend fun updateProgress(uid: String, progress: Progress)

}