package com.gcancino.levelingup.presentation.init

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcancino.levelingup.data.models.Patient
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.domain.usecases.AuthUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InitScreenViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val _userState = MutableStateFlow<Resource<Patient>>(Resource.Loading())
    val userState: StateFlow<Resource<Patient>> = _userState.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            delay(2000)
            authUseCase.getCurrentUser().collect { resource ->
                _userState.value = resource
            }
        }
    }
}