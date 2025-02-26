package com.gcancino.levelingup.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gcancino.levelingup.domain.database.converters.Converters
import com.gcancino.levelingup.domain.database.dao.PatientDao
import com.gcancino.levelingup.domain.database.dao.QuestDao
import com.gcancino.levelingup.domain.database.entities.DailyQuestEntity
import com.gcancino.levelingup.domain.database.entities.ExerciseProgressEntity
import com.gcancino.levelingup.domain.database.entities.PatientEntity

@Database(
    entities = [DailyQuestEntity::class, ExerciseProgressEntity::class, PatientEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questDao(): QuestDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}