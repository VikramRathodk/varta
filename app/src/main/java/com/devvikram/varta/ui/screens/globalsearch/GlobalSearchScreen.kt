package com.devvikram.varta.ui.screens.globalsearch

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.devvikram.varta.ui.composable.reusables.BackNavigationButton
import com.devvikram.varta.ui.composable.reusables.MySearchBar


@Composable
fun GlobalSearchScreen(
    navHostController: NavHostController
){
    val list = mutableListOf<String>()
    for (i in 1..100) {
        list.add("Item $i")
    }
    val searchQuery= remember { mutableStateOf("") }
    Scaffold (
        topBar = {
            GlobalSearchToolBar(
                searchQuery,
                navHostController
            )
        }
    ){
        paddingValues ->

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)

        ){
            Text(text = "Global Search Screen")
            Text(text = searchQuery.value)
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalSearchToolBar(searchQuery: MutableState<String>,navHostController: NavHostController) {
    TopAppBar(
        title = {
            MySearchBar(
                query = searchQuery.value,
                onBackPressed = {
                    navHostController.popBackStack()
                } ,
                placeholder = "Search...",
                onQueryChanged = {
                    searchQuery.value = it
                },
                isBackNavigationEnabled = false
            )
        },
        navigationIcon = {
            BackNavigationButton{
                navHostController.popBackStack()
            }
        }
    )
}
