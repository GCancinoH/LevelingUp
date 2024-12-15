package com.gcancino.levelingup.data.models.patient.progress

object Levels {
    val BEGINNER = mapOf(
        1 to 0, 2 to 250, 3 to 500, 4 to 1000, 5 to 1250, 6 to 1500, 7 to 1750, 8 to 2000,
        9 to 2500, 10 to 3000
    )

    val INTERMEDIATE = mapOf(
        1 to 0, 2 to 1000, 3 to 2000, 4 to 3000, 5 to 4000, 6 to 5000, 7 to 6000, 8 to 7000,
        9 to 8000, 10 to 10000
    )

    val ADVANCED = mapOf(
        1 to 0, 2 to 2000, 3 to 4000, 4 to 6000, 5 to 8000, 6 to 10000, 7 to 12000, 8 to 14000,
        9 to 16000, 10 to 20000
    )
}