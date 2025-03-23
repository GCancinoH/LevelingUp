package com.gcancino.levelingup.domain.usecases

import com.gcancino.levelingup.data.repositories.auth.AuthRepositoryImp
import com.gcancino.levelingup.domain.database.dao.PlayerDao

class AuthUseCase(
    private val repository: AuthRepositoryImp
) {
    suspend fun getPlayerFromDB() = repository.getCurrentPlayerFromDB()

    suspend fun signInWithEmail(email: String, password: String) =
        repository.signInWithEmail(email, password)

    fun signInWithGoogle() =
        repository.signInWithGoogle()

    suspend fun signUpWithEmail(email: String, password: String, name: String, playerDao: PlayerDao) =
        repository.signUpWithEmail(email, password, name, playerDao)

    fun signUpWithGoogle() =
        repository.signInWithGoogle()

    fun signOut() = repository.signOut()

    fun getPatientName() = repository.getPatientName()

    fun isPatientEmailVerified() = repository.isPatientEmailVerified()
}