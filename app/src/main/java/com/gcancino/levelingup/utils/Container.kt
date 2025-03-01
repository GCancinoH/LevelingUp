package com.gcancino.levelingup.utils

import android.content.Context
import androidx.room.Room
import com.gcancino.levelingup.data.repositories.BloodPressureBSRepository
import com.gcancino.levelingup.data.repositories.BodyCompositionBSRepository
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
import com.gcancino.levelingup.presentation.user.dashboard.BloodPressureViewModel
import com.gcancino.levelingup.presentation.user.dashboard.BodyCompositionBSViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Container(
    private val context: Context
) {
    // Firebase Init
    val auth by lazy { FirebaseAuth.getInstance() }
    val db by lazy { FirebaseFirestore.getInstance() }
    val storage by lazy { FirebaseStorage.getInstance() }

    // Local Database initialization
    private val appDatabase : AppDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    // DAOs
    val bodyCompositionDao by lazy { appDatabase.bodyCompositionDao() }
    val bloodPressureDao by lazy { appDatabase.bloodPressureDao() }

    // Repositories
    private val authRepository by lazy { AuthRepositoryImp() }
    private val authUseCase by lazy { AuthUseCase(authRepository) }
    private val improvementRepository by lazy { ImprovementRepository() }
    private val improvementUseCase by lazy { ImprovementUseCase(improvementRepository) }
    private val bodyCompositionRepository by lazy { BodyCompositionBSRepository(bodyCompositionDao, db )}
    private val bloodPressureRepository by lazy { BloodPressureBSRepository(bloodPressureDao, db )}

    // View Models
    val initViewModel by lazy { InitScreenViewModel(authUseCase) }
    val signInViewModel by lazy { SignInViewModel(authUseCase) }
    val signUpViewModel by lazy { SignUpViewModel(authUseCase) }
    val improvementViewModel by lazy { ImprovementViewModel(improvementUseCase, authUseCase) }
    val initialDataViewModel by lazy { InitialDataViewModel() }
    val bodyCompositionViewModel by lazy { BodyCompositionBSViewModel(
        repository = bodyCompositionRepository,
        auth = auth
    ) }
    val bloodPressureViewModel by lazy { BloodPressureViewModel(
        repository = bloodPressureRepository,
        auth = auth
    ) }



}