package com.gcancino.levelingup.presentation.init

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcancino.levelingup.data.models.Patient
import com.gcancino.levelingup.data.repositories.PlayerRepository
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.domain.usecases.AuthUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InitScreenViewModel(
    private val authUseCase: AuthUseCase,
    private val playerRepository: PlayerRepository
) : ViewModel() {
    private val _userState = MutableStateFlow<Resource<Patient>>(Resource.Loading())
    val userState: StateFlow<Resource<Patient>> = _userState.asStateFlow()

    private val _isPlayerDataSavedLocally = MutableStateFlow<Resource<Unit>>(Resource.Loading())
    val isPlayerDataSavedLocally: StateFlow<Resource<Unit>> = _isPlayerDataSavedLocally.asStateFlow()

    init {
        //checkPlayerStatus()
        checkUserAuthentication()
    }

    private fun checkUserAuthentication() {
        viewModelScope.launch {
            _userState.update { Resource.Loading() }
            val result = playerRepository.getCurrentPlayer()
            if (result is Resource.Success && result.data == null) {
                _userState.update { Resource.Error("Player is not logged in") }
            } else {
                _userState.update { result }
            }

            if (result is Resource.Success && result.data != null) {
                isPlayerSavedLocally()
            }

        }
    }

    private fun isPlayerSavedLocally() {
        viewModelScope.launch {
            _isPlayerDataSavedLocally.update { Resource.Loading() }
            val result = playerRepository.savePlayerDataLocally()
            Log.d("InitScreenViewModel", "isPlayerSavedLocally: $result")
            _isPlayerDataSavedLocally.update { result }
        }
    }

    private fun checkPlayerStatus() {
        viewModelScope.launch {
            _userState.update { Resource.Loading() }
            val result = authUseCase.getPlayerFromDB()

            _userState.update { result }

            if (result is Resource.Success && result.data != null) {
                isPlayerSavedLocally()
            }

        }

    }
}