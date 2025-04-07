package com.gcancino.levelingup.presentation.user.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcancino.levelingup.data.repositories.BodyCompositionBSRepository
import com.gcancino.levelingup.data.repositories.BodyCompositionData
import com.gcancino.levelingup.data.repositories.QuestRepository
import com.gcancino.levelingup.domain.database.dao.DailyQuestDao
import com.gcancino.levelingup.domain.database.entities.DailyQuestEntity
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.domain.preferences.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class DashboardViewModel(
    private val bodyCompositionRepository: BodyCompositionBSRepository,
    private val auth: FirebaseAuth,
    private val questRepository: QuestRepository,
    private val questDao: DailyQuestDao,
    private val storeManager: DataStoreManager
) : ViewModel() {

    val playerID = auth.currentUser?.uid ?: ""

    private val _questLoadingState = MutableStateFlow<Resource<Unit>>(Resource.Loading())
    val questLoadingState = _questLoadingState.asStateFlow()

    private val _dailyQuests = MutableStateFlow<List<DailyQuestEntity>>(emptyList())
    val dailyQuests: Flow<List<DailyQuestEntity>> = _dailyQuests.asStateFlow()

    init {
        initializeQuestsData()
        getAllQuests()
        observeDailyQuests()
    }

    fun getBodyCompositionData(): Flow<List<BodyCompositionData>> {
        return bodyCompositionRepository.getBodyCompositionDataLocally(playerID)
    }

    fun deleteDataByID(id: UUID) {
        viewModelScope.launch {
            bodyCompositionRepository.deleteDataByID(id)
        }
    }

    private fun initializeQuestsData() {
        viewModelScope.launch {
            _questLoadingState.update { Resource.Loading() }

            val result = questRepository.initializeQuests()
            _questLoadingState.update { result }

            if (result is Resource.Error) {
                Log.e("DashboardViewModel", "Failed to initialize quests: ${result.message}")
            }

            Log.d("DashboardViewModel", "Quests initialized: $result")
        }
    }

    private fun getAllQuests() {
        viewModelScope.launch {
            questDao.getAllQuests().collectLatest { quests ->
                Log.d("DashboardViewModel", "Quests fetched: $quests")
                _dailyQuests.value = quests
            }
        }
    }

    private fun observeDailyQuests() {
        questRepository.observeQuest().onEach { quests ->
            _dailyQuests.value = quests
        }.launchIn(viewModelScope)
    }

}