package com.gcancino.levelingup.data.repositories.auth

//import android.util.Log
import com.gcancino.levelingup.data.models.Patient
import com.gcancino.levelingup.data.models.patient.Progress
import com.gcancino.levelingup.data.models.patient.progress.CategoryType
import com.gcancino.levelingup.domain.database.dao.PlayerDao
import com.gcancino.levelingup.domain.database.entities.PlayerEntity
import com.gcancino.levelingup.domain.entities.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date

class AuthRepositoryImp() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    
    suspend fun getCurrentPlayerFromDB(): Resource<Patient> {
        val patient = auth.currentUser
        return when(patient) {
            null -> Resource.Error("Player not found")
            else -> Resource.Success(
                Patient(
                    uid = patient.uid,
                    email = patient.email ?: "",
                    displayName = patient.displayName ?: "",
                )
            )
        }
    }

    suspend fun signInWithEmail(email: String, password: String): Resource<Patient> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val patient = result.user
            when (patient) {
                null -> Resource.Error("Authentication failed")
                else -> Resource.Success(
                    Patient(
                        uid = patient.uid,
                        email = patient.email ?: "",
                        displayName = patient.displayName ?: "",
                    )
                )
            }
        } catch (e: FirebaseAuthException) {
            val errorMessage = when (e.errorCode) {
                "ERROR_INVALID_CREDENTIALS" -> "Invalid login credentials"
                "ERROR_USER_DISABLED" -> "User has been disabled"
                "ERROR_USER_NOT_FOUND" -> "User not found"
                "ERROR_WRONG_PASSWORD" -> "Wrong password"
                "ERROR_INVALID_EMAIL" -> "Invalid email"
                "ERROR_EMAIL_ALREADY_IN_USE" -> "Email already in use"
                else -> e.localizedMessage ?: "Authentication failed"
            }
           Resource.Error(errorMessage)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    fun signInWithGoogle()
    {
        /* TODO() */
    }

    suspend fun signUpWithEmail(
        email: String,
        password: String,
        displayName: String,
        playerDao: PlayerDao
    ): Resource<Patient> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val player = result.user
            when (player) {
                null -> Resource.Error("User creation failed")
                else -> {
                    val recentPlayer = Patient(
                        uid = player.uid,
                        email = player.email ?: "",
                        displayName = displayName,
                        progress = Progress(
                            level = 1,
                            exp = 0,
                            currentCategory = CategoryType.CATEGORY_BEGINNER
                        )
                    )

                    // Save to Firestore
                    db.collection("players").document(player.uid)
                        .set(recentPlayer)
                        .await()

                    // Save to Room DB
                    val playerEntity = PlayerEntity(
                        uid = player.uid,
                        email = player.email ?: "",
                        displayName = displayName,
                        height = 0.0,
                        progress = recentPlayer.progress,
                        lastSync = Date(),
                        needsSync = false
                    )
                    playerDao.insertPlayer(playerEntity)

                    Resource.Success(recentPlayer)
                    //patient.updateDisplayName(displayName).await()
                    //patient.sendEmailVerification().await()
                }
            }
        } catch (e: FirebaseAuthException) {
            val errorMessage = when (e.errorCode) {
                "ERROR_INVALID_CREDENTIALS" -> "Invalid login credentials"
                "ERROR_USER_DISABLED" -> "User has been disabled"
                "ERROR_USER_NOT_FOUND" -> "User not found"
                "ERROR_WRONG_PASSWORD" -> "Wrong password"
                "ERROR_INVALID_EMAIL" -> "Invalid email"
                "ERROR_EMAIL_ALREADY_IN_USE" -> "Email already in use"
                else -> e.localizedMessage ?: "Authentication failed"
            }
            Resource.Error(errorMessage)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    fun signOut(): Resource<Unit> {
        return try {
            auth.signOut()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Sign out failed")
        }
    }

    fun getPatientName() : String {
        val patient = auth.currentUser
        return when (patient) {
            null -> ""
            else -> patient.displayName ?: ""
        }
    }

    fun isPatientEmailVerified() : Boolean? {
        val patient = auth.currentUser
        return patient?.isEmailVerified
    }

}