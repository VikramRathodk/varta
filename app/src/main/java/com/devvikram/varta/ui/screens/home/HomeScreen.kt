package com.devvikram.varta.ui.screens.home

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devvikram.varta.AppViewModel
import com.devvikram.varta.config.constants.Destination
import com.devvikram.varta.ui.screens.contacts.ContactViewModel
import com.devvikram.varta.ui.screens.contacts.ContactsScreen
import com.devvikram.varta.ui.screens.conversations.ConversationScreen
import com.devvikram.varta.ui.screens.conversations.ConversationViewmodel
import com.devvikram.varta.ui.screens.conversations.FloatingAccctionButton
import com.devvikram.varta.ui.screens.conversations.HomeToolBar
import com.devvikram.varta.ui.screens.conversations.LogoutDialog
import com.devvikram.varta.ui.screens.conversations.UserDetailsBottomView
import com.devvikram.varta.ui.screens.creategroup.CreateGroupViewModel

@Composable
fun HomeScreen(
    appViewModel: AppViewModel,
    mainNavController: NavHostController
) {
    val homeViewModel : HomeViewModel = hiltViewModel()
    val currentLoggedUser by homeViewModel.currentLoggedUser.collectAsState(initial = null)
    val isDarkMode = isSystemInDarkTheme()
    val isLogoutVisible by homeViewModel.isLogoutVisible.collectAsState()
    val isHomeToolbarVisible by homeViewModel.isHomeToolbarVisible.collectAsState()

    val homeNavController = rememberNavController()
    Surface {
        Scaffold (
            topBar = {
                if(isHomeToolbarVisible){
                    HomeToolBar(
                        modifier = Modifier,
                        homeViewModel = homeViewModel,
                        onSearchIconClick = {
                            mainNavController.navigate(Destination.GlobalSearch)
                        }
                    )
                }
            },
            floatingActionButton = {
                if(isHomeToolbarVisible){
                    FloatingAccctionButton(
                        openContactsList = {
                            homeViewModel.setIsHomeToolbarVisible(false)
                            homeNavController.navigate(Destination.Contact)
                        })
                }
            },
            bottomBar = {



                UserDetailsBottomView(modifier = Modifier.navigationBarsPadding(),isDarkMode = isDarkMode,currentLoggedUser, onLogout = {
                    appViewModel.updateLoginStatus(false)
                })
            },
        ){ paddingValues ->

            NavHost(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(paddingValues),
                navController = homeNavController,
                startDestination = Destination.Conversation
            ){
                composable<Destination.Conversation> {
                    val conversationViewmodel : ConversationViewmodel = hiltViewModel()
                    ConversationScreen(
                        conversationViewmodel = conversationViewmodel,
                        homeNavigationController = homeNavController,
                        mainNavController = mainNavController
                    )
                }

                composable<Destination.Contact> {
                    val contactViewModel : ContactViewModel = hiltViewModel()
                    val groupCreateViewModel : CreateGroupViewModel = hiltViewModel()

                    ContactsScreen(
                        navHostController = mainNavController,
                        contactViewModel = contactViewModel,
                        groupCreateViewModel = groupCreateViewModel,
                        onBackClick = {
                            homeViewModel.setIsHomeToolbarVisible(true)
                            homeNavController.popBackStack()
                        }
                    )

                }
            }
        }
    }


    //         log out dialog
    if(isLogoutVisible){
        LogoutDialog(
            appViewModel = appViewModel,
            homeViewModel = homeViewModel
        )
    }else{
        // No dialog is open, do nothing
        println("Do not open logout dialog...")
    }
}