package com.gcancino.levelingup.presentation.auth.signin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcancino.levelingup.data.models.Patient
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.domain.usecases.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    // Injectors
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isPasswordVisible by mutableStateOf(false)
        private set
    private val _authState = MutableStateFlow<Resource<Patient>?>(null)
    var authState : StateFlow<Resource<Patient>?> = _authState.asStateFlow()

    // Methods
    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
    }

    fun onPasswordVisibilityChange() {
        isPasswordVisible = !isPasswordVisible
    }

    fun signIn() {
        _authState.value = Resource.Loading(null)
        viewModelScope.launch {
            try {
                authUseCase.signInWithEmail(email, password).collect { resource ->
                    _authState.value = resource
                }
            } catch (e: Exception) {
                _authState.value = Resource.Error(e.localizedMessage ?: "Sign in failed")
            }
        }
    }

    fun signInWithGoogle() {}


}

