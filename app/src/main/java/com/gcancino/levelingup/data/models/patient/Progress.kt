package com.gcancino.levelingup.data.models.patient

import com.gcancino.levelingup.data.models.patient.progress.CategoryType
import java.util.Date

data class Progress(
    val coins: Int = 0,
    val level : Int = 0,
    val exp: Int = 0,
    val currentCategory: CategoryType = CategoryType.CATEGORY_BEGINNER,
    val lastLevelUpdate : Date? = null,
    val lastCategoryUpdate : Date? = null
)

