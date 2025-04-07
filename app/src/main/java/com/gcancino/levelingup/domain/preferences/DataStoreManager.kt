package com.gcancino.levelingup.domain.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gcancino.levelingup.data.models.quests.QuestType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

val Context.userPreferencesStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class DataStoreManager(
    private val context: Context
) {
    private val dataStore = context.userPreferencesStore

    private object PreferencesKey {
        val ARE_QUESTS_LOADED = booleanPreferencesKey("are_quests_loaded")
        val LAST_QUEST_RESET = stringPreferencesKey("last_quest_reset")
        val SELECTED_IMPROVEMENTS = stringPreferencesKey("selected_improvements")
        val IS_PLAYER_DATA_SAVED_LOCALLY = booleanPreferencesKey("is_player_data_saved_locally")
    }

    val userPreferences: Flow<UserPreferences> = dataStore.data.map { preferences ->
        UserPreferences(
            areDailyQuestsLoaded = preferences[PreferencesKey.ARE_QUESTS_LOADED] ?: false,
            lastQuestReset = preferences[PreferencesKey.LAST_QUEST_RESET]?.let {
                LocalDate.parse(it)
            } ?: LocalDate.MIN,
            selectedImprovements = preferences[PreferencesKey.SELECTED_IMPROVEMENTS]
                ?.split(",")
                ?.mapNotNull { QuestType.valueOf(it) }
                ?.toSet() ?: emptySet(),
            isPlayerDataSavedLocally = preferences[PreferencesKey.IS_PLAYER_DATA_SAVED_LOCALLY] ?: false
        )
    }

    suspend fun updateQuestLoadedStatus(date: LocalDate) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.ARE_QUESTS_LOADED] = true
            preferences[PreferencesKey.LAST_QUEST_RESET] = date.toString()
        }
    }

    suspend fun updatePlayerDataSavedStatus(isSaved: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.IS_PLAYER_DATA_SAVED_LOCALLY] = isSaved
        }
    }

    suspend fun changeQuestLoadedStatus(status: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.ARE_QUESTS_LOADED] = status
        }
    }
}