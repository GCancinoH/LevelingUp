package com.gcancino.levelingup.presentation.auth.signup

//import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.gcancino.levelingup.R
import com.gcancino.levelingup.domain.entities.Resource
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    snackBarHostState: SnackbarHostState,
    onSignUpSuccess: () -> Unit,
    onSignInBtnClick: () -> Unit,

) {
    val authState by viewModel.authState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(authState) {
        when (val state = authState) {
            is Resource.Success -> onSignUpSuccess
            is Resource.Error -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = state.message ?: "Sign up failed",
                        duration = SnackbarDuration.Short
                    )
                }
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.levelingup),
            contentDescription = "Logo",
            modifier = Modifier.width(150.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Google SignIn Button
        OutlinedButton(
            onClick = { viewModel.signUpWithGoogle() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContainerColor = Color.Unspecified,
                disabledContentColor = Color.Unspecified
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = "Google Logo",
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sign In with Google")
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Divider OR
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f)
                    .padding(end = 8.dp)
            )
            Text(text = "OR")
            HorizontalDivider(
                modifier = Modifier.weight(1f)
                    .padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Name TextField
        OutlinedTextField(
            value = viewModel.name,
            onValueChange = { viewModel.onNameChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Name"
                )
            },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            isError = viewModel.nameError != null,
            supportingText = {
                if (viewModel.nameError != null) {
                    Text(text = viewModel.nameError!!,
                        color = MaterialTheme.colorScheme.error)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Email TextField
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.onEmailChange(it) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "Email"
                )
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = viewModel.emailError != null,
            supportingText = {
                if (viewModel.emailError != null) {
                    Text(text = viewModel.emailError!!,
                        color = MaterialTheme.colorScheme.error)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Password"
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { viewModel.onPasswordVisibilityChange() }
                ) {
                    Icon(
                        imageVector = if (viewModel.isPasswordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff,
                        contentDescription = "Password Visibility"
                    )
                }
            },
            visualTransformation = if (viewModel.isPasswordVisible)
                VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isError = viewModel.passwordError != null,
            supportingText = {
                if (viewModel.passwordError != null) {
                    Text(text = viewModel.passwordError!!,
                        color = MaterialTheme.colorScheme.error)
                }
            }
        )

        // SignIn Button
        Button(
            onClick = { viewModel.signUp() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonColors(
                containerColor = Color(0xFF850000),
                contentColor = Color.White,
                disabledContainerColor = Color.Unspecified,
                disabledContentColor = Color.Unspecified
            )
        ) {
            when(authState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                else -> Text("Sign Up")
            }
        }

        // Account Creation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(text = "Don't have an account?")
            TextButton(onClick = { onSignInBtnClick }) {
                Text(text = "Sign Up")
            }
        }
    }

}