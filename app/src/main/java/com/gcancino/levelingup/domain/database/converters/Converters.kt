package com.gcancino.levelingup.domain.database.converters

import androidx.room.TypeConverter
import com.gcancino.levelingup.data.models.quests.QuestDetails
import com.gcancino.levelingup.data.models.quests.QuestRewards
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

}