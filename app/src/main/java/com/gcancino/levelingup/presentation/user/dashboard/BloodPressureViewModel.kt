package com.gcancino.levelingup.presentation.user.dashboard

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcancino.levelingup.data.repositories.BloodPressureBSRepository
import com.gcancino.levelingup.data.repositories.BloodPressureData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

class BloodPressureViewModel(
    private val repository: BloodPressureBSRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    val bloodPressureData = repository.data

    var systolic by mutableStateOf("")
    var diastolic by mutableStateOf("")
    var pulsePerMin by mutableStateOf("")

    val today = Date()

    fun saveDataLocally() {
        val playerID = auth.currentUser?.uid

        val data = BloodPressureData(
            id = UUID.randomUUID().toString(),
            playerID = playerID ?: "",
            systolic = systolic.toInt(),
            diastolic = diastolic.toInt(),
            pulsePerMin = pulsePerMin.toInt(),
            date = today
        )

        viewModelScope.launch {
            repository.saveDataLocally(data)
        }

    }
}