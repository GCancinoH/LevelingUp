package com.gcancino.levelingup.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gcancino.levelingup.presentation.auth.signin.SignInScreen
import com.gcancino.levelingup.presentation.auth.signup.SignUpScreen
import com.gcancino.levelingup.presentation.auth.signup.improvements.ImprovementScreen
import com.gcancino.levelingup.presentation.init.InitScreen
import com.gcancino.levelingup.presentation.user.dashboard.DashboardScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun Navigation(

) {
    val navController: NavHostController = rememberNavController()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val appContainer = Container()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "improvements",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("initScreen") {
                InitScreen(
                    viewModel = appContainer.initViewModel,
                    onSignedIn = {
                        navController.navigate("dashboard") {
                            popUpTo("initScreen") {
                                inclusive = true
                            }
                        }
                    },
                    onSignInError = {
                        navController.navigate("signIn") {
                            popUpTo("initScreen") {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable("signIn") {
                SignInScreen(
                    viewModel = appContainer.signInViewModel,
                    navController = navController,
                    snackBarHostState = snackbarHostState,
                )
            }
            composable("signUp") {
                SignUpScreen(
                    viewModel = appContainer.signUpViewModel,
                    snackBarHostState = snackbarHostState,
                    onSignUpSuccess = { navController.navigate("improvementScreen") },
                    onSignInBtnClick = { navController.navigate("signIn") }
                )
            }
            composable("improvements") {
                ImprovementScreen(
                    viewModel = appContainer.improvementViewModel
                )
            }
            composable("forgotPassword") {
                /* TODO() */
            }
            composable("dashboard") {
                DashboardScreen()
            }
        }
    }
}

