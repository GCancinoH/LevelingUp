package com.gcancino.levelingup.presentation.user.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcancino.levelingup.data.repositories.BodyCompositionBSRepository
import com.gcancino.levelingup.data.repositories.BodyCompositionData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

class DashboardViewModel(
    private val bodyCompositionRepository: BodyCompositionBSRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    val playerID = auth.currentUser?.uid ?: ""

    fun getBodyCompositionData(): Flow<List<BodyCompositionData>> {
        return bodyCompositionRepository.getBodyCompositionDataLocally(playerID)
    }

    fun deleteDataByID(id: UUID) {
        viewModelScope.launch {
            bodyCompositionRepository.deleteDataByID(id)
        }
    }

}