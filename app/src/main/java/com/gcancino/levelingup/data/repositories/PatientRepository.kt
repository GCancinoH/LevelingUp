package com.gcancino.levelingup.data.repositories

import com.gcancino.levelingup.data.models.quests.QuestType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PatientRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val patientCollection = db.collection("patients")


    fun getUserImprovements(): Flow<Set<QuestType>> = callbackFlow {
        val patientID = auth.currentUser?.uid
        val listener = patientCollection.document(patientID!!)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Firestore error: ${error.message}")
                    close(error)
                    return@addSnapshotListener
                }

                val improvements = snapshot?.get("improvements") as? List<*>
                val questTypes = improvements?.mapNotNull { improvement ->
                    (improvement as? String)?.let {
                        try { QuestType.valueOf(it) } catch (e: Exception) { null }
                    }
                }?.toSet() ?: emptySet()

                trySend(questTypes)
            }
        awaitClose { listener }
    }
}