package com.gcancino.levelingup.utils

import com.gcancino.levelingup.data.repositories.auth.AuthRepositoryImp
import com.gcancino.levelingup.domain.usecases.AuthUseCase
import com.gcancino.levelingup.presentation.auth.signin.SignInViewModel
import com.gcancino.levelingup.presentation.auth.signup.SignUpViewModel
import com.gcancino.levelingup.presentation.init.InitScreenViewModel

class Container {
    private val authRepository by lazy { AuthRepositoryImp() }
    private val authUseCase by lazy { AuthUseCase(authRepository) }

    // Viewmodels
    val initViewModel by lazy { InitScreenViewModel(authUseCase) }
    val signInViewModel by lazy { SignInViewModel(authUseCase) }
}