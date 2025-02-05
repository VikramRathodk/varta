package com.devvikram.varta.ui.screens.groupchatroom.commoncomposable

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devvikram.varta.R

@Composable
fun AddNewMemberRow(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.varta_logo),
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape),
            alignment = Alignment.CenterStart
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold
        ), modifier = Modifier
            .fillMaxWidth()
            .clickable {
                println("Clicked on Add New Member")
                onClick()
            })
    }

}