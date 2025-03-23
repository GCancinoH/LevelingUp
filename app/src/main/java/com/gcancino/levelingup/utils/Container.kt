package com.gcancino.levelingup.utils

import android.content.Context
import androidx.room.Room
import com.gcancino.levelingup.domain.preferences.DataStoreManager
import com.gcancino.levelingup.data.repositories.BloodPressureBSRepository
import com.gcancino.levelingup.data.repositories.BodyCompositionBSRepository
import com.gcancino.levelingup.data.repositories.PlayerRepository
import com.gcancino.levelingup.data.repositories.QuestRepository
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
import com.gcancino.levelingup.presentation.user.dashboard.DashboardViewModel
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
    val storeManager by lazy { DataStoreManager(context) }

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
    val questDao by lazy { appDatabase.questDao() }
    val exerciseDB by lazy { appDatabase.exerciseProgressDao() }
    val playerDao by lazy { appDatabase.playerDao() }

    // Repositories
    private val authRepository by lazy { AuthRepositoryImp() }
    private val authUseCase by lazy { AuthUseCase(authRepository) }
    private val improvementRepository by lazy { ImprovementRepository() }
    private val improvementUseCase by lazy { ImprovementUseCase(improvementRepository) }
    private val bodyCompositionRepository by lazy { BodyCompositionBSRepository(bodyCompositionDao, db )}
    private val bloodPressureRepository by lazy { BloodPressureBSRepository(bloodPressureDao, db )}
    private val playerRepository by lazy { PlayerRepository(auth, db, playerDao) }
    private val questRepository by lazy {
        QuestRepository(db, storeManager, playerRepository, questDao, exerciseDB)
    }

    // View Models
    val initViewModel by lazy { InitScreenViewModel(authUseCase) }
    val signInViewModel by lazy { SignInViewModel(authUseCase) }
    val signUpViewModel by lazy { SignUpViewModel(authUseCase, playerDao) }
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
    val dashboardViewModel by lazy {
        DashboardViewModel(
            bodyCompositionRepository = bodyCompositionRepository,
            auth = auth

        )
    }




}