package com.gcancino.levelingup.presentation.auth.signup.initialData

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcancino.levelingup.data.models.patient.Improvement
import com.gcancino.levelingup.data.repositories.improvement.ImprovementRepository
import com.gcancino.levelingup.domain.entities.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Locale

class InitialDataViewModel(
) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val repository = ImprovementRepository()

    var name by mutableStateOf("")
    var weight by mutableStateOf("")
    var height by mutableStateOf("")
    var bmi by mutableStateOf("")
    var birthdate by mutableStateOf<Date?>(null)
    var age by mutableIntStateOf(0)
    var visceralFat by mutableStateOf("")
    var fatPercentage by mutableStateOf("")
    var musclePercentage by mutableStateOf("")
    var progress by mutableFloatStateOf(0.2f)
    val progressIncrement = 0.2f
    var bmiInterpretation by mutableStateOf("")
    var currentStep by mutableIntStateOf(0)

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

    private val _selectedImprovements = mutableStateListOf<Improvement>()
    val selectedImprovements: List<Improvement> = _selectedImprovements

    private val _saveState = MutableStateFlow<Resource<Unit>?>(null)
    val saveState: StateFlow<Resource<Unit>?> = _saveState.asStateFlow()

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

    fun onWeightChange(newWeight: String) {
        weight = newWeight
    }

    fun onHeightChange(newHeight: String) {
        height = newHeight
        calculateBMI()
    }

    fun onBirthdateChange(newBirthdate: Date) {
        birthdate = newBirthdate
    }

    fun onVisceralFatChange(newVisceralFat: String) {
        visceralFat = newVisceralFat
    }

    fun onFatPercentageChange(newFatPercentage: String) {
        fatPercentage = newFatPercentage
    }

    fun onNameChange(newName: String) { name = newName }

    fun onMusclePercentageChange(newMusclePercentage: String) {
        musclePercentage = newMusclePercentage
    }

    fun toggleImprovement(improvement: Improvement) {
        if (_selectedImprovements.contains(improvement)) {
            _selectedImprovements.remove(improvement)
        } else {
            _selectedImprovements.add(improvement)
        }
    }

    private fun calculateBMI() {
        val weightValue = weight.toDoubleOrNull()
        val heightValue = height.toDoubleOrNull()

        if (weightValue != null && heightValue != null) {
            val bmiValue = (weightValue / (heightValue * heightValue))
            bmi = String.format(Locale.getDefault(), "%.1f", bmiValue)
            bmiInterpretation = interpretBMI(bmiValue)
        } else {
            bmi = ""
            bmiInterpretation = ""
        }
    }

    private fun calculateAge(birthdate: Date?): Int {
        if (birthdate == null) return 0
        val currentDate = Date()
        val birthdateCalendar = Calendar.getInstance()
        birthdateCalendar.time = birthdate
        val currentDateCalendar = Calendar.getInstance()
        currentDateCalendar.time = currentDate
        return (currentDateCalendar.get(Calendar.YEAR) - birthdateCalendar.get(Calendar.YEAR))
    }

    fun interpretBMI(bmi: Double): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi < 25 -> "Normal weight"
            bmi < 30 -> "Overweight"
            else -> "Obese"
        }
    }

    fun saveInitialData() {
        viewModelScope.launch {
            val currentDate = Date()
            age = calculateAge(birthdate)

            // create initial data map
            val initialData = mapOf(
                "weight" to weight.toDoubleOrNull(),
                "bmi" to bmi.toDoubleOrNull(),
                "visceralFat" to visceralFat.toDoubleOrNull(),
                "fatPercentage" to fatPercentage.toDoubleOrNull(),
                "musclePercentage" to musclePercentage,
                "photos" to null,
                "date" to currentDate
            )

            val patientData = mapOf(
                "height" to height.toDoubleOrNull(),
                "age" to age,
                "gender" to selectedGender.value,
                "objectives" to selectedObjective.value,
                "birthday" to birthdate,
                "improvements" to selectedImprovements,
                "initialData" to initialData,
                "displayName" to name,
            )

            repository.saveInitialData(patientData, photos.value).collect { resource ->
                _saveState.value = resource
                Log.d("InitialDataViewModel", "Resource: $resource")
            }

        }
    }
}