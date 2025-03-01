package com.gcancino.levelingup.presentation.user.dashboard

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcancino.levelingup.data.repositories.BodyCompositionBSRepository
import com.gcancino.levelingup.data.repositories.BodyCompositionData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class BodyCompositionBSViewModel(
    private val repository: BodyCompositionBSRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    val bodyCompositionData = repository.data

    var weight by mutableStateOf("")
    var bmi by mutableStateOf("")
    var bodyFat by mutableStateOf("")
    var muscleMass by mutableStateOf("")
    var visceralFat by mutableStateOf("")
    var bodyAge by mutableStateOf("")
    val today = LocalDate.now()

    fun saveBodyCompositionData() {
        val userID = auth.currentUser?.uid
        val bodyCompData = BodyCompositionData(
            id = UUID.randomUUID(),
            playerID = userID ?: "",
            weight = weight.toDouble(),
            bmi = bmi.toDouble(),
            bodyFat = bodyFat.toDouble(),
            muscleMass = muscleMass.toDouble(),
            visceralFat = visceralFat.toInt(),
            bodyAge = bodyAge.toInt(),
            date = today
        )

        viewModelScope.launch {
            repository.saveBodyCompositionData(bodyCompData)
        }
    }

    fun saveDataLocally() {
        if (weight.isEmpty() || bmi.isEmpty() || bodyFat.isEmpty() || muscleMass.isEmpty() || visceralFat.isEmpty() || bodyAge.isEmpty()) {
            return
        }

        val userID = auth.currentUser?.uid
        val bodyCompData = BodyCompositionData(
            id = UUID.randomUUID(),
            playerID = userID ?: "",
            weight = weight.toDouble(),
            bmi = bmi.toDouble(),
            bodyFat = bodyFat.toDouble(),
            muscleMass = muscleMass.toDouble(),
            visceralFat = visceralFat.toInt(),
            bodyAge = bodyAge.toInt(),
            date = today
        )

        viewModelScope.launch {
            repository.saveDataLocally(bodyCompData)
        }
    }
}