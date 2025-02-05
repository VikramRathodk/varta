package com.devvikram.varta.ui.composable.reusables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun MySearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    placeholder: String = "Search...",
    onBackPressed: () -> Unit,
    isBackNavigationEnabled: Boolean = false
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(60.dp),
        color = MaterialTheme.colorScheme.surface.copy(
            alpha = 0.8f
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(60.dp)
                )
                .padding(horizontal = 8.dp, vertical = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(isBackNavigationEnabled){
                IconButton(onClick = {
                    onBackPressed()
                    onQueryChanged("")
                }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back Icon")
                }
            }
//
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = query,
                onValueChange = onQueryChanged,
                placeholder = {
                    Text(
                        text = placeholder,
                    )
                },

                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(60.dp))
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                    disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.1f),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            if (query.isNotEmpty()) {
                IconButton(onClick = {
                    onQueryChanged("")
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Search",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }else{
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
            }
        }
    }

}
