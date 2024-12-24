package com.gcancino.levelingup.presentation.auth.signup.initialData

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Date

class InitialDataViewModel(

) : ViewModel() {
    var weight by mutableStateOf("")
    var height by mutableStateOf("")
    var bmi by mutableStateOf("")
    var birthdate by mutableStateOf<Date?>(null)
    var age by mutableStateOf("")
    var gender by mutableStateOf("")
    var visceralFat by mutableStateOf("")
    var fatPercentage by mutableStateOf("")
    var musclePercentage by mutableStateOf("")
    var photos by mutableStateOf<List<Uri>>(emptyList())

    var currentStep by mutableIntStateOf(0)
    var showBMI by mutableStateOf(false)

    fun nextStep() { currentStep++ }

    fun previousStep() { currentStep-- }

    fun onWeightChange(newWeight: String) { weight = newWeight }
    fun onHeightChange(newHeight: String) { height = newHeight }
    fun onBMIChange(newBMI: String) { bmi = newBMI }
    fun onBirthdateChange(newBirthdate: Date) { birthdate = newBirthdate }

}