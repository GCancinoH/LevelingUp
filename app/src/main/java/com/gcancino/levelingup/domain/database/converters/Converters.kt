package com.gcancino.levelingup.domain.database.converters

import androidx.room.TypeConverter
import com.gcancino.levelingup.data.models.quests.QuestDetails
import com.gcancino.levelingup.data.models.quests.QuestRewards
import com.gcancino.levelingup.data.models.quests.QuestStatus
import com.gcancino.levelingup.data.models.quests.QuestType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromMap(map: Map<String, Any>): String {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun toMap(mapString: String): Map<String, Any> {
        return Gson().fromJson(mapString, object : TypeToken<Map<String, Any>>() {}.type)
    }

    @TypeConverter
    fun questDetailsToString(details: QuestDetails): String = gson.toJson(details)

    @TypeConverter
    fun stringToQuestDetails(json: String): QuestDetails =
        gson.fromJson(json, QuestDetails::class.java)

    @TypeConverter
    fun fromQuestDetails(details: QuestDetails): String {
        return gson.toJson(details)
    }

    @TypeConverter
    fun toQuestDetails(json: String): QuestDetails = gson.fromJson(json, QuestDetails::class.java)

    @TypeConverter
    fun fromQuestDetailsList(detailsList: List<QuestDetails>): String {
        return gson.toJson(detailsList)
    }

    @TypeConverter
    fun fromQuestRewards(rewards: QuestRewards): String = gson.toJson(rewards)

    @TypeConverter
    fun toQuestRewards(json: String): QuestRewards = gson.fromJson(json, QuestRewards::class.java)

    @TypeConverter
    fun fromQuestTpe(type: QuestType?): String? = type?.name

    @TypeConverter
    fun toQuestType(value: String?): QuestType? = value?.let { QuestType.valueOf(it) }

    @TypeConverter
    fun fromQuestTypes(types: List<QuestType>?): String? =
        types?.joinToString(",") { it.name }

    @TypeConverter
    fun toQuestTypes(value: String?): List<QuestType>? =
        value?.split(",")?.map { QuestType.valueOf(it) }

    @TypeConverter
    fun fromQuestStatus(status: QuestStatus): String = status.name

    @TypeConverter
    fun toQuestStatus(value: String): QuestStatus = QuestStatus.valueOf(value)

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun fromQuestDetails(details: QuestDetails?): String? =
        details?.let { gson.toJson(it) }

    @TypeConverter
    fun toQuestDetails(value: String?): QuestDetails? = value?.let {
        gson.fromJson(it, QuestDetails::class.java)
    }
}