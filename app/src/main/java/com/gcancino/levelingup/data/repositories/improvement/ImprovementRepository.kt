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
            emit(Resource.Error(e.localizedMessage ?: "Error saving improvements"))
        }
    }
}