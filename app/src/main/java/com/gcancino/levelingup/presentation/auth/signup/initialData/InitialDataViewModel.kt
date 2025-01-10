package com.gcancino.levelingup.presentation.auth.signup.initialData

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class InitialDataViewModel(

) : ViewModel() {
    var weight by mutableStateOf("")
    var height by mutableStateOf("")
    var bmi by mutableStateOf("")
    var birthdate by mutableStateOf<Date?>(null)
    var age by mutableStateOf("")
    var visceralFat by mutableStateOf("")
    var fatPercentage by mutableStateOf("")
    var musclePercentage by mutableStateOf("")
    var progress by mutableFloatStateOf(0.125f)
    val progressIncrement = 0.125f

    var currentStep by mutableIntStateOf(0)
    var showBMI by mutableStateOf(false)

    private val _objectives = listOf(
        "Lose Body Fat",
        "Increase Muscle Mass",
        "Maintenance",
        "Body Recomposition"
    )

    private val _gender = listOf(
        "Male",
        "Female"
    )

    private val _selectedObjective = MutableStateFlow<String?>(null)
    val selectedObjective: StateFlow<String?> = _selectedObjective

    private val _selectedGender = MutableStateFlow<String?>(null)
    val selectedGender: StateFlow<String?> = _selectedGender

    private val _photos = MutableStateFlow<List<Uri>>(emptyList())
    val photos: StateFlow<List<Uri>> = _photos

    private val _tmpPhoto = MutableStateFlow<Uri?>(null)
    val tmpPhoto: StateFlow<Uri?> = _tmpPhoto

    fun selectObjective(option: String) {
        _selectedObjective.value = option
    }

    fun selectGender(option: String) {
        _selectedGender.value = option
    }

    fun getObjectives(): List<String> = _objectives
    fun getGender(): List<String> = _gender

    fun nextStep() {
        currentStep++
        progress += progressIncrement
    }

    fun previousStep() {
        currentStep--
        progress -= progressIncrement
    }

    fun addPhotos(newPhotos: List<Uri>) {
        viewModelScope.launch {
            _photos.emit(_photos.value + newPhotos)
        }
    }

    fun removePhoto(photo: Uri) {
        viewModelScope.launch {
            _photos.emit(_photos.value.filter { it != photo })
        }
    }

    fun onWeightChange(newWeight: String) { weight = newWeight }
    fun onHeightChange(newHeight: String) { height = newHeight }
    fun onBMIChange(newBMI: String) { bmi = newBMI }
    fun onBirthdateChange(newBirthdate: Date) { birthdate = newBirthdate }
    fun onVisceralFatChange(newVisceralFat: String) { visceralFat = newVisceralFat }
    fun onFatPercentageChange(newFatPercentage: String) { fatPercentage = newFatPercentage }
    fun onMusclePercentageChange(newMusclePercentage: String) { musclePercentage = newMusclePercentage }

}