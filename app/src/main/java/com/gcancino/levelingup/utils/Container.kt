package com.gcancino.levelingup.utils

import android.content.Context
import androidx.room.Room
import com.gcancino.levelingup.data.repositories.auth.AuthRepositoryImp
import com.gcancino.levelingup.data.repositories.improvement.ImprovementRepository
import com.gcancino.levelingup.domain.database.AppDatabase
import com.gcancino.levelingup.domain.usecases.AuthUseCase
import com.gcancino.levelingup.domain.usecases.ImprovementUseCase
import com.gcancino.levelingup.presentation.auth.signin.SignInViewModel
import com.gcancino.levelingup.presentation.auth.signup.SignUpViewModel
import com.gcancino.levelingup.presentation.auth.signup.improvements.ImprovementViewModel
import com.gcancino.levelingup.presentation.auth.signup.initialData.InitialDataViewModel
import com.gcancino.levelingup.presentation.init.InitScreenViewModel

class Container(
    private val context: Context
) {
    private val authRepository by lazy { AuthRepositoryImp() }
    private val authUseCase by lazy { AuthUseCase(authRepository) }
    private val improvementRepository by lazy { ImprovementRepository() }
    private val improvementUseCase by lazy { ImprovementUseCase(improvementRepository) }

    // View Models
    val initViewModel by lazy { InitScreenViewModel(authUseCase) }
    val signInViewModel by lazy { SignInViewModel(authUseCase) }
    val signUpViewModel by lazy { SignUpViewModel(authUseCase) }
    val improvementViewModel by lazy { ImprovementViewModel(improvementUseCase, authUseCase) }
    val initialDataViewModel by lazy { InitialDataViewModel() }


    private val appDatabase : AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    //val patientDao by lazy { appDatabase.patientDao() }
}