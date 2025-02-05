package com.devvikram.varta.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme


// Light and Dark Color Schemes
val LightColorScheme = lightColorScheme(
    primary = ProColors.LightPrimary,
    onPrimary = ProColors.LightOnPrimary,
    primaryContainer = ProColors.LightPrimaryContainer,
    onPrimaryContainer = ProColors.LightOnPrimaryContainer,
    secondary = ProColors.LightSecondary,
    onSecondary = ProColors.LightOnSecondary,
    secondaryContainer = ProColors.LightSecondaryContainer,
    onSecondaryContainer = ProColors.LightOnSecondaryContainer,
    tertiary = ProColors.LightTertiary,
    onTertiary = ProColors.LightOnTertiary,
    tertiaryContainer = ProColors.LightTertiaryContainer,
    onTertiaryContainer = ProColors.LightOnTertiaryContainer,
    error = ProColors.LightError,
    onError = ProColors.LightOnError,
    errorContainer = ProColors.LightErrorContainer,
    onErrorContainer = ProColors.LightOnErrorContainer,
    background = ProColors.LightBackground,
    onBackground = ProColors.LightOnBackground,
    surface = ProColors.LightSurface,
    onSurface = ProColors.LightOnSurface,
    surfaceVariant = ProColors.LightSurfaceVariant,
    onSurfaceVariant = ProColors.LightOnSurfaceVariant,
    outline = ProColors.LightOutline,
    inverseOnSurface = ProColors.LightInverseOnSurface,
    inverseSurface = ProColors.LightInverseSurface,
    inversePrimary = ProColors.LightInversePrimary
)

val DarkColorScheme = darkColorScheme(
    primary = ProColors.DarkPrimary,
    onPrimary = ProColors.DarkOnPrimary,
    primaryContainer = ProColors.DarkPrimaryContainer,
    onPrimaryContainer = ProColors.DarkOnPrimaryContainer,
    secondary = ProColors.DarkSecondary,
    onSecondary = ProColors.DarkOnSecondary,
    secondaryContainer = ProColors.DarkSecondaryContainer,
    onSecondaryContainer = ProColors.DarkOnSecondaryContainer,
    tertiary = ProColors.DarkTertiary,
    onTertiary = ProColors.DarkOnTertiary,
    tertiaryContainer = ProColors.DarkTertiaryContainer,
    onTertiaryContainer = ProColors.DarkOnTertiaryContainer,
    error = ProColors.DarkError,
    onError = ProColors.DarkOnError,
    errorContainer = ProColors.DarkErrorContainer,
    onErrorContainer = ProColors.DarkOnErrorContainer,
    background = ProColors.DarkBackground,
    onBackground = ProColors.DarkOnBackground,
    surface = ProColors.DarkSurface,
    onSurface = ProColors.DarkOnSurface,
    surfaceVariant = ProColors.DarkSurfaceVariant,
    onSurfaceVariant = ProColors.DarkOnSurfaceVariant,
    outline = ProColors.DarkOutline,
    inverseOnSurface = ProColors.DarkInverseOnSurface,
    inverseSurface = ProColors.DarkInverseSurface,
    inversePrimary = ProColors.DarkInversePrimary
)

// AppTheme Composable Function
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()

    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    ) {
        content()
    }
}

// Previews for Light and Dark Themes
@Preview(showBackground = true)
@Composable
fun LightThemePreview() {
    AppTheme {
        // Your composables here to preview the theme
    }
}

@Preview(showBackground = true)
@Composable
fun DarkThemePreview() {
    AppTheme {
        // Your composables here to preview the theme
    }
}
