package com.gcancino.levelingup.presentation.init

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gcancino.levelingup.domain.entities.Resource
import com.gcancino.levelingup.R
import com.gcancino.levelingup.data.models.Patient

@Composable
fun InitScreen(
    viewModel: InitScreenViewModel,
    onSignedIn: () -> Unit,
    onSignInError: () -> Unit,
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()
    val isPlayerSavedLocally by viewModel.isPlayerDataSavedLocally.collectAsStateWithLifecycle()

    if (userState is Resource.Loading || isPlayerSavedLocally is Resource.Loading) {
        InitialLoadingContent(isPlayerSavedLocally, userState)
    }

    LaunchedEffect(userState) {
        when (val state = userState) {
            is Resource.Success -> {
                if (state.data == null) {
                    onSignInError()
                }
            }
            is Resource.Error -> onSignInError()
            else -> null
        }
    }

    LaunchedEffect(isPlayerSavedLocally) {
        if (userState is Resource.Success) {
            when (isPlayerSavedLocally) {
                is Resource.Success -> onSignedIn()
                is Resource.Error -> onSignInError()
                else -> null
            }
        }
    }
}

@Composable
fun InitialLoadingContent(
    isPlayerSavedLocally: Resource<Unit>,
    userState: Resource<Patient>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        /*Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp)
        )*/
        Spacer(modifier = Modifier.height(16.dp))
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        if(userState is Resource.Loading) {
            Text(text = "Checking user status...")
        } else if (isPlayerSavedLocally is Resource.Loading) {
            Text(text = "Checking if player data is saved locally...")
        }


    }
}

