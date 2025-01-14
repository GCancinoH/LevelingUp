package com.gcancino.levelingup.data.repositories.improvement

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.gcancino.levelingup.domain.entities.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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

    fun saveInitialData(patientData: Map<String, Any>, photoList: List<Uri>): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val currentDate = Date()
            val patientID = auth.currentUser?.uid
            val photoUrls = mutableListOf<String>()

            // Upload photos to Firebase Storage
            for (photo in photoList) {
                val fileName = "${UUID.randomUUID()}.jpg}"
                val photoRef = storage.reference.child("patient_photos/$patientID/initialPhotos/$fileName")
                photoRef.putFile(photo).await()
                val downloadTask = photoRef.downloadUrl.await().toString()
                photoUrls.add(downloadTask)
            }

            val initialData = patientData["initialData"] as? Map<*, *>
            val mutableInitialData = initialData?.toMutableMap()
            mutableInitialData?.put("photos", photoUrls)
            mutableInitialData?.put("date", currentDate)

            db.collection("patients").document(patientID?: "")
                .update(patientData)
                .addOnSuccessListener { emit(Resource.Success(Unit)) }
                .addOnFailureListener { emit(Resource.Error("Error saving the data: ${it.message}")) })


        } catch (e: FirebaseFirestoreException) {
        }
    }
}