package com.gcancino.levelingup.data.models.patient

import com.gcancino.levelingup.data.models.patient.progress.CategoryType
import java.util.Date

data class Progress(
    val level : Int,
    val exp: Int,
    val currentCategory: CategoryType,
    val lastLevelUpdate : Date? = null,
    val lastCategoryUpdate : Date? = null
)

