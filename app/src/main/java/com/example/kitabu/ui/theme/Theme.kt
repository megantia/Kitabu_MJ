package com.example.kitabu.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// The khanga palette is designed for a light surface, so there is no dark scheme or dynamic colour.
private val KitabuColorScheme = lightColorScheme(
    primary = Indigo,
    onPrimary = Color.White,
    secondary = Gold,
    onSecondary = Indigo,
    tertiary = AvailableGreen,
    onTertiary = OnAvailableGreen,
    background = Lavender,
    onBackground = DeepIndigo,
    surface = Color.White,
    onSurface = DeepIndigo,
    surfaceVariant = Color.White,
    onSurfaceVariant = Indigo,
    outline = Indigo,
    error = ErrorRed,
    onError = Color.White,
    inverseSurface = DeepIndigo,
    inverseOnSurface = Color.White
)

@Composable
fun KitabuTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KitabuColorScheme,
        typography = Typography,
        content = content
    )
}
