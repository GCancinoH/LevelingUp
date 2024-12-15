package com.gcancino.levelingup.data.models.patient

import java.util.Date

data class Streak(
    val currentStreak: Int,
    val longestStreak: Int,
    val lastStreakUpdate: Date? = null
)
