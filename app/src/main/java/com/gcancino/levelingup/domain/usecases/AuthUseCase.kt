package com.gcancino.levelingup.domain.usecases

import com.gcancino.levelingup.data.repositories.auth.AuthRepositoryImp

class AuthUseCase(
    private val repository: AuthRepositoryImp
) {
    fun getCurrentUser() = repository.getCurrentUser()

    fun signInWithEmail(email: String, password: String) =
        repository.signInWithEmail(email, password)

    fun signInWithGoogle(idToken: String) =
        repository.signInWithGoogle(idToken)

    fun signUpWithEmail(name: String, email: String, password: String) =
        repository.signUpWithEmail(name, email, password)

    fun signUpWithGoogle(idToken: String) =
        repository.signInWithGoogle(idToken)

    fun signOut() = repository.signOut()
}