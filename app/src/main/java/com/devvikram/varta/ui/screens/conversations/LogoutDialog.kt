package com.devvikram.varta.ui.screens.conversations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.devvikram.varta.AppViewModel
import com.devvikram.varta.ui.screens.home.HomeViewModel

@Composable
fun LogoutDialog(
    homeViewModel: HomeViewModel,
    appViewModel: AppViewModel
) {
    Dialog(
        onDismissRequest = {
            homeViewModel.setIsLogoutVisible(true)
        }
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 10.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Are you sure you want to logout?",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            homeViewModel.setIsLogoutVisible(false)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        elevation = ButtonDefaults.buttonElevation(8.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Button(
                        onClick = {
                            appViewModel.updateLoginStatus(false)
                            homeViewModel.setIsLogoutVisible(false)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        elevation = ButtonDefaults.buttonElevation(8.dp)
                    ) {
                        Text(
                            text = "Logout",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.error
                            )
                        )
                    }
                }
            }
        }
    }
}