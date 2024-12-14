package com.gcancino.levelingup.data.repositories.auth

import com.gcancino.levelingup.data.models.Patient
import com.gcancino.levelingup.domain.entities.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp() {
    private val auth = FirebaseAuth.getInstance()
    
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
                    Patient(
                        uid = it.uid,
                        email = it.email ?: "",
                        displayName = it.displayName ?: "",
                    )

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

    fun signInWithGoogle(idToken: String)
    {
        /* TODO() */
    }

    fun signUpWithEmail(name: String, email: String, password: String) : Flow<Resource<Patient>> = flow {
        emit(Resource.Loading())
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val patient = result.user
            when (patient) {
                null -> emit(Resource.Error("Authentication failed"))
                else -> {
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

}