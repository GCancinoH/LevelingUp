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
import com.gcancino.levelingup.data.models.Patient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.Date
import java.util.UUID

class InitialDataViewModel(

) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    var weight by mutableStateOf("")
    var height by mutableStateOf("")
    var bmi by mutableDoubleStateOf(0.0)
    var birthdate by mutableStateOf<Date?>(null)
    var age by mutableIntStateOf(0)
    var visceralFat by mutableStateOf("")
    var fatPercentage by mutableStateOf("")
    var musclePercentage by mutableStateOf("")
    var progress by mutableFloatStateOf(0.125f)
    val progressIncrement = 0.125f

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

    fun onWeightChange(newWeight: String) {
        weight = newWeight
    }

    fun onHeightChange(newHeight: String) {
        height = newHeight
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

    fun onMusclePercentageChange(newMusclePercentage: String) {
        musclePercentage = newMusclePercentage
    }

    private fun calculateBMI(): Double {
        val weightValue = weight.toDoubleOrNull() ?: return 0.0
        val heightValue = height.toDoubleOrNull() ?: return 0.0
        return (weightValue / (heightValue * heightValue))
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

    fun saveInitialData() {
        viewModelScope.launch {
            val patientID = auth.currentUser?.uid
            val currentDate = Date()

            bmi = calculateBMI()
            age = calculateAge(birthdate)

            // Upload photos to Firebase Storage
            val photoUrls = mutableListOf<String>()
            for (photo in photos.value) {
                val fileName = "${UUID.randomUUID()}.jpg}"
                val photoRef = storage.reference.child("patient_photos/$patientID/initialPhotos/$fileName")
                photoRef.putFile(photo).await()
                val downloadTask = photoRef.downloadUrl.await().toString()
                photoUrls.add(downloadTask)
            }

            // create initial data map
            val initialData = mapOf(
                "weight" to weight,
                "bmi" to bmi,
                "visceralFat" to visceralFat,
                "fatPercentage" to fatPercentage,
                "musclePercentage" to musclePercentage,
                "photos" to photoUrls,
                "date" to currentDate
            )

            val patientData = mapOf(
                "height" to height,
                "age" to age,
                "gender" to selectedGender.value,
                "objectives" to selectedObjective.value,
                "initialData" to initialData
            )
        }
    }




}