package com.gcancino.levelingup.data.repositories

import com.gcancino.levelingup.domain.database.dao.BodyCompositionDao
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.utils.toDomainModel
import com.gcancino.levelingup.utils.toEntityModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.util.UUID

data class BodyCompositionData(
    val id: UUID,
    val playerID: String,
    val weight: Double,
    val bmi: Double,
    val bodyFat: Double,
    val muscleMass: Double,
    val visceralFat: Int,
    val bodyAge: Int,
    val date: LocalDate
)

class BodyCompositionBSRepository(
    private val localDB: BodyCompositionDao,
    private val db: FirebaseFirestore
) {

    private val _data = MutableStateFlow<Resource<BodyCompositionData>?>(null)
    val data: StateFlow<Resource<BodyCompositionData>?> = _data.asStateFlow()

    suspend fun saveDataLocally(bodyComposition: BodyCompositionData) {
        _data.value = Resource.Loading()
        try {
            localDB.insertBodyComposition(bodyComposition.toEntityModel())
            _data.value = Resource.Success(bodyComposition)
        } catch (e: Exception) {
            _data.value = Resource.Error(e.message ?: "Unknown error occurred")
        }
    }

    fun getBodyCompositionDataLocally(playerID: String) : Flow<List<BodyCompositionData>> {
        return localDB.getAllBodyCompositions(playerID)
            .map { entities -> entities.map { it.toDomainModel() } }
    }

    fun getBodyCompositionLocallyByID(id: UUID, playerID: String): Flow<BodyCompositionData?> {
        return localDB.getBodyComposition(id, playerID)
            .map { entity -> entity?.toDomainModel() }
    }

    suspend fun deleteDataByID(id: UUID) {
        return localDB.deleteDataByID(id)
    }

    fun hasEntryForToday(playerID: String): Flow<Boolean> {
        val today = LocalDate.now()
        return localDB.checkIfEntryExists(playerID, today)
    }

    suspend fun saveBodyCompositionData(data: BodyCompositionData) {
        _data.value = Resource.Loading()
        try {
            db.collection("body_composition").add(data).await()
            _data.value = Resource.Success(data)
        } catch (e: Exception) {
            _data.value = Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
}