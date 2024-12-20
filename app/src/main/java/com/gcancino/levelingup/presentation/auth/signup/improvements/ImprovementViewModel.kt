package com.gcancino.levelingup.presentation.auth.signup.improvements

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcancino.levelingup.data.models.patient.Improvement
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.domain.usecases.AuthUseCase
import com.gcancino.levelingup.domain.usecases.ImprovementUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ImprovementViewModel(
    private val improvementUseCase: ImprovementUseCase,
    private val authUseCase: AuthUseCase
) : ViewModel() {
    private val _selectedImprovements = mutableStateListOf<Improvement>()
    val selectedImprovements: List<Improvement> = _selectedImprovements

    private val _saveState = MutableStateFlow<Resource<Unit>?>(null)
    val saveState: MutableStateFlow<Resource<Unit>?> = _saveState

    val patientName = authUseCase.getPatientName().split(" ").getOrNull(0)


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

    fun saveImprovementsToDB() {
        _saveState.value = null
        viewModelScope.launch {
            try {
                improvementUseCase.saveImprovementIntoDB("", getSelectedImprovementNames()).collect { resource ->
                    _saveState.value = resource
                }
            } catch (e: Exception) {
                _saveState.value = Resource.Error(e.localizedMessage ?: "Error saving improvements")
            }
        }
    }

}