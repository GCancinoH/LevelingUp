package com.gcancino.levelingup.domain.database.converters

import androidx.room.TypeConverter
import com.gcancino.levelingup.data.models.patient.Attributes
import com.gcancino.levelingup.data.models.patient.Improvement
import com.gcancino.levelingup.data.models.patient.InitialData
import com.gcancino.levelingup.data.models.patient.Objectives
import com.gcancino.levelingup.data.models.patient.Progress
import com.gcancino.levelingup.data.models.patient.Streak
import com.gcancino.levelingup.data.models.quests.ExerciseProgress
import com.gcancino.levelingup.data.models.quests.ExerciseType
import com.gcancino.levelingup.data.models.quests.QuestAttributes
import com.gcancino.levelingup.data.models.quests.QuestDetails
import com.gcancino.levelingup.data.models.quests.QuestRewards
import com.gcancino.levelingup.data.models.quests.QuestStatus
import com.gcancino.levelingup.data.models.quests.QuestType
import com.gcancino.levelingup.data.models.quests.StageRequirement
import com.gcancino.levelingup.data.models.quests.StrengthProgression
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date
import java.util.UUID

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromUUID(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    fun toUUID(uuidString: String): UUID {
        return UUID.fromString(uuidString)
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate): Long {
        return date.atStartOfDay(ZoneOffset.UTC).toEpochSecond()
    }

    @TypeConverter
    fun toLocalDate(timestamp: Long): LocalDate {
        return LocalDate.ofEpochDay(timestamp)
    }

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    /*
        Daily Quest Converters
    */

    // Quest Types
    @TypeConverter
    fun fromQuestTypeList(types: List<QuestType>?): String? {
        return types?.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toQuestTypeList(value: String?): List<QuestType>? {
        return value?.split(",")?.map { QuestType.valueOf(it) }
    }

    // Quest Status
    @TypeConverter
    fun fromQuestStatus(status: QuestStatus): String = status.name

    @TypeConverter
    fun toQuestStatus(value: String): QuestStatus = QuestStatus.valueOf(value)

    // Quest Rewards
    @TypeConverter
    fun fromQuestRewards(rewards: QuestRewards): String = gson.toJson(rewards)

    @TypeConverter
    fun toQuestRewards(value: String): QuestRewards = gson.fromJson(value, QuestRewards::class.java)

    // Quest Details
    @TypeConverter
    fun fromQuestDetails(details: QuestDetails?): String? {
        if (details == null) return null

        val typeInfo = when(details) {
            is QuestDetails.StrengthQuestDetails -> "strength"
            is QuestDetails.EnduranceQuestDetails -> "endurance"
            is QuestDetails.MobilityQuestDetails -> "mobility"
            is QuestDetails.RecoveryQuestDetails -> "recovery"
            is QuestDetails.MentalToughnessQuestDetails -> "mental_toughness"
        }

        val json = gson.toJson(details)
        val wrapper = mapOf("type" to typeInfo, "data" to json)
        return gson.toJson(wrapper)
    }

    @TypeConverter
    fun toQuestDetails(value: String?): QuestDetails? {
        if (value == null) return null

        val wrapper = gson.fromJson<Map<String, String>>(
            value,
            object : TypeToken<Map<String, String>>() {}.type
        )

        val type = wrapper["type"] ?: return null
        val data = wrapper["data"] ?: return null

        return when (type) {
            "strength" -> gson.fromJson(data, QuestDetails.StrengthQuestDetails::class.java)
            "endurance" -> gson.fromJson(data, QuestDetails.EnduranceQuestDetails::class.java)
            "mobility" -> gson.fromJson(data, QuestDetails.MobilityQuestDetails::class.java)
            "recovery" -> gson.fromJson(data, QuestDetails.RecoveryQuestDetails::class.java)
            "mental_toughness" -> gson.fromJson(
                data,
                QuestDetails.MentalToughnessQuestDetails::class.java
            )

            else -> null
        }
    }

    // Quest Attributes
    @TypeConverter
    fun fromQuestAttributes(attributes: QuestAttributes?): String? =
        attributes?.let { gson.toJson(it) }

    @TypeConverter
    fun toQuestAttributes(value: String?): QuestAttributes? =
        value?.let { gson.fromJson(it, QuestAttributes::class.java) }

    // Strength Progression
    @TypeConverter
    fun fromStrengthProgression(progression: StrengthProgression?): String? =
        progression?.let { gson.toJson(it) }

    @TypeConverter
    fun toStrengthProgression(value: String?): StrengthProgression? =
        value?.let { gson.fromJson(it, StrengthProgression::class.java) }

    // Stage Requirements
    @TypeConverter
    fun fromStageRequirements(requirements: List<StageRequirement>?): String? =
        requirements?.let { gson.toJson(it) }

    @TypeConverter
    fun toStageRequirements(value: String?): List<StageRequirement>? =
        value?.let { gson.fromJson(it, object : TypeToken<List<StageRequirement>>() {}.type) }

    // Exercise Progress
    @TypeConverter
    fun fromExerciseProgressList(progressList: List<ExerciseProgress>?): String? =
        progressList?.let { gson.toJson(it) }

    @TypeConverter
    fun toExerciseProgressList(value: String?): List<ExerciseProgress>? =
        value?.let {
            gson.fromJson(it, object : TypeToken<List<ExerciseProgress>>() {}.type)
        }

    // Exercise Type
    @TypeConverter
    fun fromExerciseType(type: ExerciseType?): String? = type?.name

    @TypeConverter
    fun toExerciseType(value: String?): ExerciseType? = value?.let { ExerciseType.valueOf(it) }


    // Utilities
    @TypeConverter
    fun fromMap(map: Map<String, Any>): String {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun toMap(mapString: String): Map<String, Any> {
        return Gson().fromJson(mapString, object : TypeToken<Map<String, Any>>() {}.type)
    }

    /*
        Player Converters
    */

    // Improvement List
    @TypeConverter
    fun fromImprovementList(improvements: List<Improvement>?): String? =
        improvements?.let { gson.toJson(it) }

    @TypeConverter
    fun toImprovementList(value: String?): List<Improvement>? {
        if (value == null) return null

        val type = object : TypeToken<List<Improvement>>() {}.type
        return gson.fromJson(value, type)
    }

    // Initial Data Converter
    @TypeConverter
    fun fromInitialData(initialData: InitialData?): String? = initialData?.let { gson.toJson(it) }

    @TypeConverter
    fun toInitialData(value: String?): InitialData? {
        if (value == null) return null
        return gson.fromJson(value, InitialData::class.java)
    }

    // Objectives
    @TypeConverter
    fun fromObjectives(objectives: Objectives?): String? = objectives?.name

    @TypeConverter
    fun toObjectives(value: String?): Objectives? = value?.let { Objectives.valueOf(it) }

    // Progress
    @TypeConverter
    fun fromProgress(progress: Progress?): String = gson.toJson(progress)

    @TypeConverter
    fun toProgress(value: String?): Progress? {
        if (value == null) return null
        return gson.fromJson(value, Progress::class.java)
    }

    // Attributes
    @TypeConverter
    fun fromAttributes(attributes: Attributes?): String? = attributes?.let { gson.toJson(it) }

    @TypeConverter
    fun toAttributes(value: String?): Attributes? {
        if (value == null) return null
        return gson.fromJson(value, Attributes::class.java)
    }

    // Streak
    @TypeConverter
    fun fromStreak(streak: Streak?): String? = streak?.let { gson.toJson(it) }

    @TypeConverter
    fun toStreak(value: String?): Streak? {
        if(value == null) return null
        return gson.fromJson(value, Streak::class.java)
    }
}