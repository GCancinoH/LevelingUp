package com.gcancino.levelingup.presentation.auth.signup.improvements

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.gcancino.levelingup.data.models.patient.Improvement

class ImprovementViewModel(

) : ViewModel() {
    private val _selectedImprovements = mutableStateListOf<Improvement>()
    val selectedImprovements: List<Improvement> = _selectedImprovements

    fun toggleImprovement(improvement: Improvement) {
        if (_selectedImprovements.contains(improvement)) {
            _selectedImprovements.remove(improvement)
        } else {
            _selectedImprovements.add(improvement)
        }
    }

    fun getSelectedImprovementNames(): List<String> {
        return selectedImprovements.map { it.name }
    }

    fun saveImprovementsToFirestore() {

    }

}