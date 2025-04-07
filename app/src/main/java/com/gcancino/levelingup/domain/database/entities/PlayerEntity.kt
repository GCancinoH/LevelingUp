package com.gcancino.levelingup.domain.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gcancino.levelingup.data.models.patient.Attributes
import com.gcancino.levelingup.data.models.patient.Improvement
import com.gcancino.levelingup.data.models.patient.InitialData
import com.gcancino.levelingup.data.models.patient.Objectives
import com.gcancino.levelingup.data.models.patient.Progress
import com.gcancino.levelingup.data.models.patient.Streak
import java.util.Date

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey val uid: String,
    val displayName: String? = "",
    val email: String,
    val photoUrl: String? = "",
    val birthday: Date? = null,
    val age: Int? = null,
    val height: Double? = null,
    val gender: String? = null,
    val phoneNumber: String? = null,
    @ColumnInfo(name = "initial_data")
    val initialData: InitialData? = null,
    val improvements: List<Improvement>? = listOf(),
    val objectives: Objectives? = null,
    val progress: Progress? = null,
    val attributes: Attributes? = null,
    val streak: Streak? = null,
    @ColumnInfo(name = "last_sync")
    val lastSync: Date = Date(),
    @ColumnInfo(name = "needs_sync")
    val needsSync: Boolean = false
)
