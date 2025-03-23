package com.gcancino.levelingup.presentation.auth.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcancino.levelingup.data.models.Patient
import com.gcancino.levelingup.domain.database.dao.PlayerDao
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.domain.usecases.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authUseCase: AuthUseCase,
    private val playerDao: PlayerDao
) : ViewModel() {
    var name by mutableStateOf("")
        private set
    var nameError by mutableStateOf<String?>(null)
        private set

    var email by mutableStateOf("")
        private set
    var emailError by mutableStateOf<String?>(null)
        private set

    var password by mutableStateOf("")
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    private val _authState = MutableStateFlow<Resource<Patient>?>(null)
    var authState: StateFlow<Resource<Patient>?> = _authState.asStateFlow()

    val emailRegex = android.util.Patterns.EMAIL_ADDRESS

    fun onEmailChange(newEmail: String) {
        email = newEmail.trim()
        emailError = null
        validateEmail()
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword.trim()
        passwordError = null
        validatePassword()
    }

    fun onPasswordVisibilityChange() {
        isPasswordVisible = !isPasswordVisible
    }

    fun signUp() {
        _authState.value = Resource.Loading()
        // Check the form is valid
        if (!isValidEmail() || !isValidPassword()) {
            _authState.value = Resource.Error("Form is not valid")
            return
        }

        viewModelScope.launch {
            try {
                val result = authUseCase.signUpWithEmail(email, password, name, playerDao)
                _authState.value = result
            } catch (e: Exception) {
                _authState.value = Resource.Error(e.localizedMessage ?: "Sign up failed")
            }
        }
    }

    fun signUpWithGoogle() {
    }

    private fun validateEmail() {
        //email.isNotBlank() && emailRegex.matcher(name).matches()
        emailError = when {
            email.isBlank() -> "Email cannot be blank"
            !emailRegex.matcher(email).matches() -> "Enter a valid email"
            else -> null
        }
    }

    private fun validatePassword() {
        passwordError = when {
            password.isBlank() || password.isEmpty() -> "Password cannot be blank"
            password.length < 6 -> "Password must be at least 6 characters long"
            else -> null
        }
    }

    private fun isValidEmail(): Boolean { return emailRegex.matcher(email).matches() && (email.isNotBlank() || email.isEmpty()) }
    private fun isValidPassword(): Boolean { return password.length >= 6 && (password.isNotBlank() || password.isEmpty()) }
}