package com.gcancino.levelingup.data.repositories

import com.gcancino.levelingup.domain.database.dao.BloodPressureDao
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.utils.toDomainModel
import com.gcancino.levelingup.utils.toEntityModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.Date
import java.util.UUID

data class BloodPressureData(
    val id: String,
    val playerID: String,
    val systolic: Int,
    val diastolic: Int,
    val pulsePerMin: Int,
    val date: Date
)

class BloodPressureBSRepository(
    private val localDB: BloodPressureDao,
    private val db: FirebaseFirestore
) {

    private val _data = MutableStateFlow<Resource<BloodPressureData>?>(null)
    val data: StateFlow<Resource<BloodPressureData>?> = _data.asStateFlow()

    suspend fun saveDataLocally(data: BloodPressureData) {
        _data.value = Resource.Loading()
        try {
            localDB.insertBloodPressure(data.toEntityModel())
            _data.value = Resource.Success(data)
        } catch (e: Exception) {
            _data.value = Resource.Error(e.message ?: "Unknown error occurred")
        }
    }

    fun getBloodPressureDataLocally(playerID: String) : Flow<List<BloodPressureData>> {
        return localDB.getAllBloodPressureData(playerID)
            .map { entities -> entities.map { it.toDomainModel() } }
    }

    suspend fun deleteDataByID(id: String) {
        return localDB.deleteData(id)
    }

    fun hasEntryForToday(playerID: String): Flow<Boolean> {
        val today = LocalDate.now()
        return localDB.checkIfEntryExists(playerID, today)
    }
}