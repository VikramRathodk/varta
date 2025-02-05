package com.devvikram.varta.ui.composable.reusables.headerrow

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun HeaderRow (
    onClickListener: () -> Unit,
    title: String = "",
    iconSize : Dp = 32.dp,
    icon: Int = 0,
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(if(iconSize == 32.dp) 0.dp else 8.dp)
            .clickable { onClickListener() }
    ) {
        Row(
            modifier = Modifier.padding(
                start = if(iconSize == 32.dp) 0.dp else 8.dp,
                end = if(iconSize == 32.dp) 0.dp else 8.dp,
                top = if(iconSize == 32.dp) 4.dp else 8.dp,
                bottom = if(iconSize == 32.dp) 4.dp else 8.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape),
                colorFilter = ColorFilter.tint(
                    color = colorScheme.onSurface.copy(alpha = 0.7f)
                )
            )
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(text = title, color = colorScheme.primary)
            }
        }
    }

}