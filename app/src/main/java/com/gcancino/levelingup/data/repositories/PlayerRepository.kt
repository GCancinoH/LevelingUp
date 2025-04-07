package com.gcancino.levelingup.data.repositories

import android.util.Log
import com.gcancino.levelingup.data.models.Patient
import com.gcancino.levelingup.data.models.patient.Genders
import com.gcancino.levelingup.data.models.patient.Improvement
import com.gcancino.levelingup.domain.database.dao.PlayerDao
import com.gcancino.levelingup.domain.database.entities.PlayerEntity
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.domain.preferences.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.google.firebase.auth.FirebaseUser

fun PlayerEntity.toDomainModel() = Patient(
    uid = uid,
    displayName = displayName,
    email = email,
    photoUrl = photoUrl,
    birthday = birthday,
    age = age,
    height = height,
    gender = gender?.let { Genders.valueOf(it) },
    phoneNumber = phoneNumber,
    initialData = initialData,
    improvements = improvements,
    objectives = objectives,
    progress = progress,
    streak = streak,
)

fun Patient.toEntityModel() = PlayerEntity(
    uid = uid,
    displayName = displayName,
    email = email,
    photoUrl = photoUrl,
    birthday = birthday,
    age = age,
    height = height,
    gender = gender.toString(),
    phoneNumber = phoneNumber,
    initialData = initialData,
    improvements = improvements,
    objectives = objectives,
    progress = progress,
    streak = streak,

)

class PlayerRepository(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val playerDB: PlayerDao,
    private val storeManager: DataStoreManager
) {
    private val playerID = auth.currentUser?.uid ?: ""

    suspend fun getCurrentPlayer(): Resource<Patient> {
        return try {
            val player = auth.currentUser
            if (player != null) {
                fetchPlayerDataFromFirestore()
            } else {
                Resource.Success(null!!)
            }
        } catch (e: Exception) {
            Log.e("PlayerRepository", "Failed to get current player: ${e.message}")
            Resource.Error(e.message ?: "Failed to get current player")
        }
    }

    suspend fun getPlayerImprovements(): List<Improvement> {
        val playerID = auth.currentUser?.uid ?: ""
        return if (playerID.isNotEmpty()) {
            try {
                withContext(Dispatchers.IO) {
                    val player = playerDB.getPlayerByID(playerID)
                    player?.improvements ?: emptyList()
                }
            } catch(e: Exception) {
                Log.e("PlayerRepository", "Failed to get player improvements: ${e.message}")
                emptyList()
            }

        } else {
            emptyList()
        }
    }

    suspend fun savePlayerDataLocally(): Resource<Unit> {
        Resource.Loading(Unit)
        return try {
            val userPreferences = storeManager.userPreferences.first()
            if (!userPreferences.isPlayerDataSavedLocally) {
                val playerFromFirestore = fetchPlayerDataFromFirestore()
                when (playerFromFirestore) {
                    is Resource.Success -> {
                        withContext(Dispatchers.IO) {
                            playerDB.insertPlayer(playerFromFirestore.data!!.toEntityModel())
                        }
                        storeManager.updatePlayerDataSavedStatus(true)
                        return Resource.Success(Unit)
                    }
                    is Resource.Error -> {
                        Log.e("PlayerRepository", "Failed to fetch player data: ${playerFromFirestore.message}")
                        return Resource.Error(playerFromFirestore.message ?: "Failed to fetch player data")
                    }
                    else -> {
                        Log.e("PlayerRepository", "Unknown error fetching player data")
                        return Resource.Error("Unknown error")
                    }
                }
            } else {
                Resource.Success(Unit)
            }
        } catch (e: Exception) {
            Log.d("PlayerRepository", "Failed to save player data locally: ${e.message}")
            Resource.Error(e.message ?: "Failed to save player data locally")
        }
    }

    private suspend fun fetchPlayerDataFromFirestore(): Resource<Patient> {
        return try {
            val playerSnapshot = db.collection("patients").document(playerID).get().await()
            val player = playerSnapshot.toObject(Patient::class.java)
            Resource.Success(player!!)
        } catch (e: Exception) {
            Log.e("PlayerRepository", "Failed to fetch player data: ${e.message}")
            Resource.Error(e.message ?: "Failed to fetch player data")
        }
    }

}
/*
class PatientRepository (
    private val patientDao: PatientDao,
    private val scope: CoroutineScope,
    private val workManager: WorkManager
) {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val patientCollection = db.collection("patients")
    private var currentPlayerID: String? = null

    init {

    }

    fun fetchAndCachePlayer(uid: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val uid = auth.currentUser?.uid
            val snapshot = patientCollection.document(uid!!).get().await()
            val patient = snapshot.toObject(Patient::class.java)
            patientDao.insertPatient(patient!!.toEntity())
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    fun getPlayer(): Flow<Patient?> {
        val uid = auth.currentUser?.uid
        setCurrentUser(uid!!)
        return patientDao.getPatientByUid(uid).map { it?.toDomainModel() }
    }

    fun setCurrentUser(uid: String) {
        currentPlayerID = uid
    }


    fun updateProgress(uid: String, progress: Progress): Flow<Unit> = flow {
        patientDao.updatePatientProgress(uid, progress)
        patientDao.markForSync(uid)
    }

    private fun scheduleDailySync() {
        val syncWorkRequest = PeriodicWorkRequestBuilder<PlayerSyncWorker>(
            1, TimeUnit.DAYS,
            180, TimeUnit.MINUTES
        ).setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()

        workManager.enqueueUniquePeriodicWork(
            "player_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncWorkRequest
        )
    }

    suspend fun syncPendingChanges() {
        val playerID = currentPlayerID ?: return

        try {
            patientDao.getPendingSyncPlayer(playerID)
                .firstOrNull()?.let { player ->
                    try {
                        patientCollection.document(playerID).set(player.toDomainModel())
                            .await()

                        patientDao.markAsSynced(playerID)
                    } catch (e: Exception) {
                        Log.e("PlayerSyncWorker", "Error syncing player: ${e.message}")
                    }

            }
        } catch (e: Exception) {
            Log.e("PlayerSyncWorker", "Error getting pending sync player: ${e.message}")
        }
    }

    /*fun getUserImprovements(): Flow<Set<QuestType>> = callbackFlow {
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
    }*/
}

class PlayerSyncWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: PatientRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            repository.syncPendingChanges()
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry()
            else Result.failure()
        }
    }
}*/