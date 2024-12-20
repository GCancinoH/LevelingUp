package com.gcancino.levelingup.data.repositories.improvement

import com.gcancino.levelingup.domain.entities.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ImprovementRepository {
    private val db = FirebaseFirestore.getInstance()

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
}