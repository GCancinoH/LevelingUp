package com.gcancino.levelingup.data.repositories.auth

//import android.util.Log
import com.gcancino.levelingup.data.models.Patient
import com.gcancino.levelingup.data.models.patient.Progress
import com.gcancino.levelingup.data.models.patient.progress.CategoryType
import com.gcancino.levelingup.domain.entities.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    
    fun getCurrentUser(): Flow<Resource<Patient>> = flow {
        emit(Resource.Loading())
        val patient = auth.currentUser
        when(patient) {
            null -> emit(Resource.Error("User not found"))
            else -> {
                val patientData = patient.let {
                    Patient(
                        uid = it.uid,
                        email = it.email ?: "",
                        displayName = it.displayName ?: "",
                    )
                }
                emit(Resource.Success(patientData))
            }
        }
    }

    fun signInWithEmail(email: String, password: String): Flow<Resource<Patient>> = flow {
        emit(Resource.Loading())
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val patient = result.user
            when(patient) {
                null -> emit(Resource.Error("Authentication failed"))
                else -> patient.let {
                    val user = Patient(
                        uid = it.uid,
                        email = it.email ?: "",
                        displayName = it.displayName ?: "",
                    )
                    emit(Resource.Success(user))
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
            emit(Resource.Error(errorMessage))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }

    fun signInWithGoogle()
    {
        /* TODO() */
    }

    fun signUpWithEmail(email: String, password: String) : Flow<Resource<Patient>> = flow {
        emit(Resource.Loading())
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val patient = result.user
            when (patient) {
                null -> emit(Resource.Error("User creation failed"))
                else -> {
                    patient.let {
                        val recentPatient = Patient(
                            uid = it.uid,
                            email = it.email ?: "",
                            progress = Progress(
                                level = 1,
                                exp = 0,
                                currentCategory = CategoryType.CATEGORY_BEGINNER
                            )
                        )
                        // Save patient data
                        db.collection("patients").document(it.uid)
                            .set(recentPatient)

                        emit(Resource.Success(recentPatient))
                    }
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
            emit(Resource.Error(errorMessage))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }

    fun signOut() : Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            auth.signOut()
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
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