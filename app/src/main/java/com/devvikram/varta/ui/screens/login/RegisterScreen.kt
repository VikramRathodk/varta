package com.devvikram.varta.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devvikram.varta.R
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.data.retrofit.models.RegisterModel


@Composable
fun RegisterScreen(
    navHostController: NavHostController,
    registerViewModel: LoginViewModel
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.communication_animation))
    var messageStatus by remember { mutableStateOf("") }
    val registerState = registerViewModel.registerUiState.collectAsStateWithLifecycle()
    val isDarkMode = isSystemInDarkTheme()

    val background = if (isDarkMode) {
        Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                Color.Green.copy(alpha = 0.1f)
            )
        )
    }

    Scaffold {
        it.calculateTopPadding()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = background
                )
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 48.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animation
                LottieAnimation(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(16.dp),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                )
                Text(
                    text = "Welcome! Create a new account to get started.",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Normal, color = if(isDarkMode) Color.White else MaterialTheme.colorScheme.secondary,
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp, start = 32.dp, end = 32.dp)
                )

                if (messageStatus.isNotEmpty()) {
                    Text(
                        text = messageStatus,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                // Email TextField
                var email by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = stringResource(id = R.string.email)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    isError = messageStatus.contains("Email is required") || messageStatus.contains("User already exists"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                )

                // Password TextField
                var password by remember { mutableStateOf("") }
                var passwordVisible by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = stringResource(id = R.string.password)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    maxLines = 1,
                    isError = messageStatus == "Password is required",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    trailingIcon = {
                        val iconRes = if (passwordVisible) {
                            painterResource(id = R.drawable.baseline_visibility_24)
                        } else {
                            painterResource(id = R.drawable.baseline_visibility_off_24)
                        }
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Image(
                                painter = iconRes,
                                contentDescription = if (passwordVisible) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                }
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                    ),
                )


                // Terms and Conditions checkbox
                var agreedToTerms by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = agreedToTerms,
                        onCheckedChange = { agreedToTerms = it }
                    )
                    Text(
                        text = "I agree to the Terms and Conditions",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                // Sign Up Button
                var isLoading by remember { mutableStateOf(false) }
                OutlinedButton(
                    onClick = {
                            isLoading = true
                            val registerModel =  RegisterModel(
                                username = email,
                                password = password,
                            )
                            registerViewModel.register(registerModel)
                        println("onclick")

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(32.dp)
                        )
                    } else {
                        Text(text = stringResource(id = R.string.sign_up),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                            )
                        )
                    }
                }

                TextButton(
                    onClick = {
                        navHostController.navigate(Destination.Login)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                ) {
                    Text(
                        text = "Already have an account? Sign in",
                        style = MaterialTheme.typography.bodyMedium,
                        color =if(isDarkMode) Color.White else MaterialTheme.colorScheme.primary
                    )
                }

                when (val state = registerState.value) {
                    is LoginViewModel.RegisterUiState.Ideal -> {
                        messageStatus = ""
                        isLoading = false
                    }

                    is LoginViewModel.RegisterUiState.Loading -> {
                        messageStatus = "Loading..."
                        isLoading = true
                    }

                    is LoginViewModel.RegisterUiState.Error -> {
                        messageStatus = state.message
                        isLoading = false
                    }

                    is LoginViewModel.RegisterUiState.Success -> {
                        messageStatus = "Registration successful"
                        isLoading = false
                        navHostController.navigate(Destination.Login) {
                            popUpTo(Destination.Register) { inclusive = true }
                            launchSingleTop = true
                        }

                        registerViewModel.resetRegisterState()
                    }
                }
            }

            Text(
                text = stringResource(id = R.string.copy_right_message),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
