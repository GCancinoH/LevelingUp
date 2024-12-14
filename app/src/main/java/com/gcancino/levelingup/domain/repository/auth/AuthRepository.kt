package com.gcancino.levelingup.domain.repository.auth

import com.gcancino.levelingup.data.models.Patient
import com.gcancino.levelingup.domain.entities.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): Flow<Resource<Patient>>
    suspend fun signInWithEmail(email: String, password: String): Resource<Patient>
    suspend fun signInWithGoogle(idToken: String): Resource<Patient>
    suspend fun signUpWithEmail(name: String, email: String, password: String): Resource<Patient>
    suspend fun signUpWithGoogle(idToken: String): Resource<Patient>
    suspend fun signOut(): Resource<Unit>
}