package com.gcancino.levelingup.presentation.auth.signup.initialData

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class InitialDataViewModel(

) : ViewModel() {
    var weight by mutableStateOf("")
    var height by mutableStateOf("")
    var bmi by mutableStateOf("")
    var age by mutableStateOf("")
    var gender by mutableStateOf("")
    var visceralFat by mutableStateOf("")
    var fatPercentage by mutableStateOf("")
    var musclePercentage by mutableStateOf("")
    var photos by mutableStateOf<List<Uri>>(emptyList())

    var currentStep by mutableIntStateOf(0)

    fun nextStep() { currentStep++ }

    fun previousStep() { currentStep-- }

    fun onWeightChange(newWeight: String) { weight = newWeight }
    fun onHeightChange(newHeight: String) { height = newHeight }

    fun calculateBMI() : String
    {
        val bmi = weight.toDouble() / (height.toDouble() * height.toDouble())
        return bmi.toString()
    }

    fun bmiInterpretation(bmi: String): String
    {
        val bmiDouble = bmi.toDouble()
        return when (bmiDouble) {
            in 0.0..18.4 -> "Underweight"
            in 18.5..24.9 -> "Normal weight"
            in 25.0..29.9 -> "Overweight"
            else -> "Obesity"
        }
    }

    val showBIM: Boolean = weight.isNotEmpty() && height.isNotEmpty()


}