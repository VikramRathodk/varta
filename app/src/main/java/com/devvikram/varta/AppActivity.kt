package com.devvikram.varta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.rememberNavController
import com.devvikram.varta.ui.AppTheme
import com.devvikram.varta.ui.composable.navigations.AppNavigation
import com.devvikram.varta.ui.composable.navigations.logins.LoginNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppActivity : ComponentActivity() {
    private val appViewModel: AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isLoggedIn = mutableStateOf(false)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                Surface {
                    Box {
                        if (isLoggedIn.value) {
                            AppNavigation(navController,appViewModel)
                            appViewModel.listenToConversations()
                            appViewModel.createAndGetFirebaseToken()
                        } else {
                            LoginNavigation(navController,appViewModel)
                        }
                    }
                }
            }

        }

        appViewModel.isUserLoggedIn.observeForever { isUserLoggedIn ->
            isLoggedIn.value = isUserLoggedIn
        }
    }
}
