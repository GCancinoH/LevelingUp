package com.gcancino.levelingup.utils

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gcancino.levelingup.presentation.auth.signin.SignInScreen
import com.gcancino.levelingup.presentation.auth.signup.SignUpScreen
import com.gcancino.levelingup.presentation.auth.signup.improvements.ImprovementScreen
import com.gcancino.levelingup.presentation.auth.signup.initialData.InitialDataScreen
import com.gcancino.levelingup.presentation.init.InitScreen
import com.gcancino.levelingup.presentation.user.dashboard.DashboardScreen
import kotlinx.coroutines.CoroutineScope

@ExperimentalMaterial3Api
@Composable
fun Navigation(
    context: Context
) {
    val navController: NavHostController = rememberNavController()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val appContainer = Container(context)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute !in listOf("signIn", "signUp", "forgotPassword")) {
                CenterAlignedTopAppBar(
                    title = { "" },
                    navigationIcon = {
                        if (currentRoute == "initialData" && appContainer.initialDataViewModel.currentStep > 0) {
                            IconButton(onClick = { appContainer.initialDataViewModel.previousStep() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        } else null
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "initScreen",
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
                    onNavigateToSignUp = { navController.navigate("signUp") }
                )
            }
            composable("signUp") {
                SignUpScreen(
                    viewModel = appContainer.signUpViewModel,
                    snackBarHostState = snackbarHostState,
                    onSignUpSuccess = {
                        navController.navigate("initialData") {
                            popUpTo("initScreen") {
                                inclusive = true
                            }
                        }
                    },
                    onSignInBtnClick = { navController.navigate("signIn") }
                )
            }
            composable("improvements") {
                ImprovementScreen(
                    viewModel = appContainer.improvementViewModel
                )
            }
            composable("initialData") {
                InitialDataScreen(
                    viewModel = appContainer.initialDataViewModel,
                    context = context
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

