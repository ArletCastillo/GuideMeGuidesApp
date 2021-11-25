package com.example.guidemeguidesapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = MilitaryGreen200,
    primaryVariant = DarkMilitaryGreen,
    secondary = Teal200,
    secondaryVariant = Teal700,
    onPrimary = Gray100,
    onSecondary = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = CancelRed
)

private val LightColorPalette = lightColors(
    primary = DarkMilitaryGreen,
    primaryVariant = MilitaryGreen200,
    secondary = Teal200,
    secondaryVariant = Teal700,
    onPrimary = Gray200,
    onSecondary = Color.Black,
    background = Color.White,
    onBackground = Gray200,
    onSurface = Color.Black,
    onError = CancelRed
)

@Composable
fun GuideMeGuidesAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}