package com.spn.companyapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

private val DarkColorPalette = darkColors(
    primary = Color(("#130b5c").toColorInt()),
    primaryVariant = Color(("#130b5c").toColorInt()),
    secondary = Color(("#130b5c").toColorInt())
)

private val LightColorPalette = lightColors(
    primary = Color(("#130b5c").toColorInt()),
    primaryVariant = Color(("#130b5c").toColorInt()),
    secondary = Color(("#130b5c").toColorInt())

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun CompanyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors =
//        if (darkTheme) {
//        DarkColorPalette
//    } else {
        LightColorPalette
//    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}