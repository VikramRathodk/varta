package com.devvikram.varta.ui.composable.reusables.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object CommonButtons {

    @Composable
    fun SubmitButton(onClick: () -> Unit, isEnabled: Boolean = true) {
        BaseButton(text = "Submit", backgroundColor = MaterialTheme.colorScheme.primary.copy(
            alpha = 0.5f
        ), onClick = onClick, isEnabled = isEnabled)
    }

    @Composable
    fun AddButton(onClick: () -> Unit, isEnabled: Boolean = true,text: String = "Add") {
        BaseButton(text = text, backgroundColor = Color.Green.copy(
            alpha = 0.5f
        ), onClick = onClick, isEnabled = isEnabled)
    }

    @Composable
    fun EditButton(onClick: () -> Unit, isEnabled: Boolean = true,text: String = "Edit") {
        BaseButton(text = text, backgroundColor = Color.Blue.copy(
            alpha = 0.5f
        ), onClick = onClick, isEnabled = isEnabled)
    }

    @Composable
    fun DeleteButton(onClick: () -> Unit, isEnabled: Boolean = true) {
        BaseButton(text = "Delete", backgroundColor = MaterialTheme.colorScheme.error.copy(
            alpha = 0.5f
        ), onClick = onClick, isEnabled = isEnabled)
    }
    @Composable
    fun CloseButton(onClick: () -> Unit, isEnabled: Boolean = true) {
        BaseButton(text = "Close", backgroundColor = MaterialTheme.colorScheme.error.copy(
            alpha = 0.5f
        ), onClick = onClick, isEnabled = isEnabled)
    }
    @Composable
    fun CancelButton(onClick: () -> Unit, isEnabled: Boolean = true) {
        BaseButton(text = "Cancel", backgroundColor = MaterialTheme.colorScheme.error.copy(
            alpha = 0.5f
        ), onClick = onClick, isEnabled = isEnabled)
    }
    @Composable
    fun ExitButton(text: String, onClick: () -> Unit, isEnabled: Boolean = true) {
        BaseButton(text = text, onClick = onClick, backgroundColor = MaterialTheme.colorScheme.error.copy(
            alpha = 0.5f
        ), isEnabled = isEnabled)
    }

    @Composable
    fun DisabledButton(text: String) {
        BaseButton(text = text, backgroundColor = Color.Gray, onClick = {}, isEnabled = false)
    }

    @Composable
    private fun BaseButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        backgroundColor: Color,
        contentColor: Color = Color.White,
        isEnabled: Boolean = true
    ) {
        OutlinedButton (
            onClick = onClick,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(50.dp),
            border = BorderStroke(
                width = 0.5.dp,
                color = backgroundColor
            ),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = backgroundColor,
                contentColor = contentColor,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            ),
            enabled = isEnabled
        ) {
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
