package com.devvikram.varta.ui.screens.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devvikram.varta.AppViewModel
import com.devvikram.varta.R
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.data.retrofit.models.LoginInformation


@Composable
fun LoginScreen(
    navHostController: NavHostController,
    loginViewModel: LoginViewModel,
    appViewModel: AppViewModel
) {
    val context = LocalContext.current
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.communication_animation))
    var messageStatus by remember { mutableStateOf("") }
    val loginState = loginViewModel.loginUiState.collectAsStateWithLifecycle()
    val deviceIdentifier = remember { getDeviceIdentifier(context) }

        BackHandler {
            navHostController.navigate(Destination.OnBoarding) {
                popUpTo(Destination.Login) { inclusive = true }
                launchSingleTop = true
            }
        }

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

    Scaffold { paddingValues ->
        paddingValues.calculateTopPadding()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = background
                )
                .padding(paddingValues)
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
                    text = "Hello, Welcome back! Sign in to continue.",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Normal,
                        color = if (isDarkMode) Color.White else MaterialTheme.colorScheme.secondary,
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
                val passwordFocusRequester = remember { FocusRequester() }
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = stringResource(id = R.string.email)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp )
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    isError = messageStatus == "Email is required" || messageStatus.contains("User not found"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            passwordFocusRequester.requestFocus()
                        }
                    )

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
                        .focusRequester(passwordFocusRequester)
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    maxLines = 1,
                    isError = messageStatus == "Password is required" || messageStatus == "Invalid password",
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                    ),
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
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
                                },
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                            )
                        }
                    }
                )


                // Sign In Button
                var isLoading by remember { mutableStateOf(false) }
                OutlinedButton(
                    onClick = {
                        isLoading = true

                        val loginInformation = LoginInformation(
                            email = email,
                            password = password,
                            force = false,
                            deviceType = "MOBILE",
                            deviceUniqueId = deviceIdentifier
                        )
                        loginViewModel.login(loginInformation)
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
                        Text(
                            text = stringResource(id = R.string.sign_in),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkMode) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                            )
                        )
                    }
                }

                // Forgot Password and Register Account
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.forgot_password),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Don't Have Account? Register",
                        style = TextStyle(
                            color = if (isDarkMode) Color.White else MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navHostController.navigate(Destination.Register)
                        }
                    )
                }
                var isNavigated by remember { mutableStateOf(false) }

                when (val state = loginState.value) {
                    is LoginViewModel.LoginUiState.Ideal -> {
                        messageStatus = ""
                        isLoading = false
                        isNavigated = false
                    }

                    is LoginViewModel.LoginUiState.Loading -> {
                        messageStatus = "Loading..."
                        isLoading = true
                    }

                    is LoginViewModel.LoginUiState.Error -> {
                        messageStatus = state.message
                        isLoading = false
                        isNavigated = false
                    }

                    is LoginViewModel.LoginUiState.Success -> {

                        if (!isNavigated) {
                            isNavigated = true
                            isLoading = false
                            messageStatus = "Login successful"
                            appViewModel.updateLoginStatus(true)

                            loginViewModel.resetLoginState()
                            loginViewModel.syncContacts()
                        }
                    }
                }


//                // Tagline
//                Text(
//                    text = stringResource(id = R.string.tagline),
//                    style = MaterialTheme.typography.headlineMedium,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 16.dp),
//                    textAlign = TextAlign.Center
//                )
//
//                // Tagline Description
//                Text(
//                    text = stringResource(id = R.string.tagline_description),
//                    style = MaterialTheme.typography.bodyMedium,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 16.dp),
//                    textAlign = TextAlign.Center
//                )
            }

            // Copyright Message at the bottom of the screen
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

@SuppressLint("HardwareIds")
fun getDeviceIdentifier(context: Context): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // For Android 10 and above, use Android ID
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    } else {
        // For Android 9 and below, attempt to retrieve IMEI
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.deviceId ?: "Unavailable"
    }
}
