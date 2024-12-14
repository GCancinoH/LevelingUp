package com.gcancino.levelingup.presentation.auth.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gcancino.levelingup.data.models.Patient
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.domain.usecases.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel(
    authUseCase: AuthUseCase
) : ViewModel() {
    var name by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set
    private val _authState = MutableStateFlow<Resource<Patient>?>(null)
    var authState : StateFlow<Resource<Patient>?> = _authState.asStateFlow()
}