package com.gcancino.levelingup.data.repositories

import com.gcancino.levelingup.domain.database.dao.PlayerDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/*fun PatientEntity.toDomainModel() = Patient(
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

fun Patient.toEntity() = PatientEntity(
    uid = uid,
    displayName = displayName,
    email = email,
    photoUrl = photoUrl,
    birthday = birthday,
    age = age,
    height = height,
    gender = gender?.name,
    phoneNumber = phoneNumber,
    initialData = initialData,
    improvements = improvements,
    objectives = objectives,
    progress = progress,
    attributes = attributes,
)*/
class PlayerRepository(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val playerDB: PlayerDao,
) {
    private val playerID = auth.currentUser?.uid ?: ""

    suspend fun getPlayerImprovements() {}

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