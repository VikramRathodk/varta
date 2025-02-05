package com.devvikram.varta.ui.composable.reusables.chips

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TagChipButton(text: String, onClick: () -> Unit) {
    ElevatedButton(
        onClick = onClick,
        shape = RoundedCornerShape(20),
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(20))
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(20))
            .height(30.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp,
            hoveredElevation = 6.dp
        ),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
