package com.devvikram.varta.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devvikram.varta.R
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.config.constants.LoginPreference
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState


@Composable
fun OnboardingScreen(navHostController: NavHostController) {
    val context = LocalContext.current
    val loginPreference = remember { LoginPreference(context) }

    // Check login status
    if (loginPreference.getIsLoggedIn()) {
        navHostController.navigate(Destination.Conversation) {
            popUpTo(Destination.OnBoarding) { inclusive = true }
            launchSingleTop = true
        }
        return
    }

    val pagerState = rememberPagerState(initialPage = 0)

    Box(
        modifier = Modifier.background(
            brush = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.background.copy(
                        alpha = 0.6f
                    ),
                    Color.Green.copy(
                        alpha = 0.1f
                    ),
                )
            )
        )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
            ) {
               if(pagerState.currentPage < 3){
                   TextButton(
                       onClick = {
                           navHostController.navigate(Destination.Login) {
                               popUpTo(Destination.OnBoarding) { inclusive = true }
                               launchSingleTop = true
                           }
                       },

                       modifier = Modifier
                           .align(Alignment.End)
                           .clickable {}
                           .padding(16.dp),
                   ) {
                       Text(
                           text = "Skip",
                           style = MaterialTheme.typography.bodyLarge.copy(
                               color = MaterialTheme.colorScheme.onBackground
                           )
                       )
                   }
               }

                HorizontalPager(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 24.dp),
                    state = pagerState,
                    count = 4,
                ) { page ->
                    when (page) {
                        0 -> IntroductionPage()
                        1 -> SilentModePage()
                        2 -> IntegrationPage()
                        3 -> ProfessionalismPage(navHostController)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp),
                    activeColor = MaterialTheme.colorScheme.primary,
                    inactiveColor = MaterialTheme.colorScheme.secondary.copy(
                        alpha = 0.3f
                    ),
                    indicatorWidth = 8.dp,
                    indicatorHeight = 8.dp,
                    spacing = 8.dp,
                    indicatorShape = RoundedCornerShape(4.dp)
                )
            }
        }
    }


}


@Composable
fun IntroductionPage() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.message_animation))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )

        Text(
            text = "Varta: Professional Communication",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Varta combines chatting & follow-ups, keeping your communication professional and efficient.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun SilentModePage() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.silent_mode_animation))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )
        Text(text = "Silent Mode & Tagging", style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        ))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Messages tagged to you are highlighted, even in Silent Mode. No more scrolling to find your important messages.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun IntegrationPage() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.integration))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        LottieAnimation(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )


        Text(
            text = "Seamless Integrations",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Integrate Varta with My Calendar, ProCom, Task Manager, and BcStep Modules for enhanced productivity.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun ProfessionalismPage(onStartClicked: NavHostController) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.maintain_professionalizm))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(200.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .padding(16.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )

        Text(
            text = "Maintain Professionalism", style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Admins can manage chat history and ensure the sanctity of professional communication.",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedButton(onClick = {
            onStartClicked.navigate(Destination.Login) {
                popUpTo(Destination.OnBoarding) { inclusive = true }
                launchSingleTop = true
            }
        }) {
            Text(text = "Get Started", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

