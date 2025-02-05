package com.devvikram.varta.ui.screens.conversations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devvikram.varta.ui.itemmodels.FilterOptionInConversation

@Composable
fun FilterBar(
    listOfFilterOptions: MutableList<FilterOptionInConversation>,
) {
    val selectedOption = remember { mutableStateOf(listOfFilterOptions.firstOrNull()) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(listOfFilterOptions, key = { it.name }) { filterOption ->
            FilterOption(
                filterOptionInConversation = filterOption,
                isSelected = filterOption == selectedOption.value,
                onClick = {
                    selectedOption.value = filterOption
                }
            )
        }
    }
}