package com.gcancino.levelingup.data.repositories.improvement

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.gcancino.levelingup.domain.entities.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID

class ImprovementRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun saveImprovementsToDB(uID: String, improvements: List<String>): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            db.collection("patients").document(uID)
                .update("improvements", improvements.map { it.uppercase() })
                .await()
            emit(Resource.Success(Unit))
        } catch (e: FirebaseFirestoreException) {
            val errorMessage = when (e.code) {
                FirebaseFirestoreException.Code.UNAVAILABLE -> "Service unavailable"
                FirebaseFirestoreException.Code.UNAUTHENTICATED -> "Authentication failed"
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> "Permission denied"
                FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED -> "Resource exhausted"
                FirebaseFirestoreException.Code.ABORTED -> "Transaction aborted"
                FirebaseFirestoreException.Code.ALREADY_EXISTS -> "Document already exists"
                FirebaseFirestoreException.Code.CANCELLED -> "Transaction cancelled"
                FirebaseFirestoreException.Code.NOT_FOUND -> "Document not found"
                else -> "Error saving the data: ${e.message}"
            }
            emit(Resource.Error(errorMessage))
        }
    }

    fun saveInitialData(patientData: Map<String, Any?>, photoList: List<Uri>): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val currentDate = Date()
            val patientID =
                auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

            coroutineScope {
                val photoUrls = photoList.map { uri ->
                    async(Dispatchers.IO) {
                        val fileName = "${UUID.randomUUID()}.jpg}"
                        val photoRef =
                            storage.reference.child("patient_photos/$patientID/initialPhotos/$fileName")
                        photoRef.putFile(uri).await()
                        photoRef.downloadUrl.await().toString()
                    }
                }.awaitAll()

                val updatedData = patientData.toMutableMap().apply {
                    (get("initialData") as? MutableMap<*, *>)?.also { initialData ->
                        @Suppress("UNCHECKED_CAST")
                        (initialData as? MutableMap<String, Any>)?.let { safeInitialData ->
                            safeInitialData["photos"] = photoUrls
                            safeInitialData["date"] = currentDate
                        }
                    }
                }

                try {
                    db.collection("patients").document(patientID)
                        .update(updatedData)
                        .await()
                    emit(Resource.Success(Unit))
                } catch (e: Exception) {
                    emit(Resource.Error("Error saving the data: ${e.message}"))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error processing data: ${e.message}"))
        }
    }
}