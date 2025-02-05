package com.devvikram.varta.ui.composable.navigations.logins

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devvikram.varta.AppViewModel
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.ui.screens.login.LoginScreen
import com.devvikram.varta.ui.screens.login.LoginViewModel
import com.devvikram.varta.ui.screens.login.RegisterScreen
import com.devvikram.varta.ui.screens.onboarding.OnboardingScreen

@Composable
fun LoginNavigation(mainNavController: NavHostController, appViewModel: AppViewModel) {
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginNavController = rememberNavController()

    NavHost(
        navController = loginNavController,
        startDestination = Destination.OnBoarding
    ) {

        /*
                -------------------------Onboarding Screen---------------------------------------
         */

        composable<Destination.OnBoarding> {
            OnboardingScreen(
                navHostController = loginNavController
            )
        }


        /*
                 ----------------------- Authentication -----------------------
         */

        composable<Destination.Login> {
            LoginScreen(
                navHostController = loginNavController,
                loginViewModel = loginViewModel,
                appViewModel = appViewModel
            )
        }

        composable<Destination.Register> {
            RegisterScreen(
                navHostController = loginNavController,
                registerViewModel = loginViewModel
            )
        }

    }
}