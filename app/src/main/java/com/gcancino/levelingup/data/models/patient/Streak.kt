package com.gcancino.levelingup.data.models.patient

import java.util.Date

data class Streak(
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastStreakUpdate: Date? = null
)
