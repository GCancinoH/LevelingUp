package com.gcancino.levelingup.domain.database.converters

import androidx.room.TypeConverter
import com.gcancino.levelingup.data.models.patient.Attributes
import com.gcancino.levelingup.data.models.patient.InitialData
import com.gcancino.levelingup.data.models.patient.Improvement
import com.gcancino.levelingup.data.models.patient.Objectives
import com.gcancino.levelingup.data.models.patient.Progress
import com.gcancino.levelingup.data.models.patient.Streak
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class PatientConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromDate(date: Date?): Long? = date?.time

    @TypeConverter
    fun toDate(timestamp: Long?): Date? = timestamp?.let { Date(it) }

    @TypeConverter
    fun fromInitialData(data: InitialData?): String? = data?.let { gson.toJson(it) }

    @TypeConverter
    fun toInitialData(json: String?): InitialData? =
        json?.let { gson.fromJson(it, InitialData::class.java) }

    @TypeConverter
    fun fromImprovements(improvements: List<Improvement>?): String? =
        gson.toJson(improvements)

    @TypeConverter
    fun toImprovements(json: String): List<Improvement> =
        gson.fromJson(json, object : TypeToken<List<Improvement>>() {}.type)

    @TypeConverter
    fun fromObjective(objective: Objectives?): String? = objective?.name

    @TypeConverter
    fun toObjective(value: String?): Objectives? = value?.let { Objectives.valueOf(it) }

    @TypeConverter
    fun fromProgress(progress: Progress?): String? = gson.toJson(progress)

    @TypeConverter
    fun toProgress(json: String?): Progress? =
        json?.let { gson.fromJson(it, Progress::class.java) }

    @TypeConverter
    fun fromAttributes(attributes: Attributes?): String? = gson.toJson(attributes)

    @TypeConverter
    fun toAttributes(json: String?): Attributes? =
        json?.let { gson.fromJson(it, Attributes::class.java) }

    @TypeConverter
    fun fromStreak(streak: Streak?): String? = gson.toJson(streak)

    @TypeConverter
    fun toStreak(json: String?): Streak? =
        json?.let { gson.fromJson(it, Streak::class.java) }
}