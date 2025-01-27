package com.gcancino.levelingup.domain.usecases

import com.gcancino.levelingup.data.repositories.auth.AuthRepositoryImp

class AuthUseCase(
    private val repository: AuthRepositoryImp
) {
    fun getCurrentUser() = repository.getCurrentUser()

    fun signInWithEmail(email: String, password: String) =
        repository.signInWithEmail(email, password)

    fun signInWithGoogle() =
        repository.signInWithGoogle()

    fun signUpWithEmail(email: String, password: String) =
        repository.signUpWithEmail(email, password)

    fun signUpWithGoogle() =
        repository.signInWithGoogle()

    fun signOut() = repository.signOut()

    fun getPatientName() = repository.getPatientName()

    fun isPatientEmailVerified() = repository.isPatientEmailVerified()
}