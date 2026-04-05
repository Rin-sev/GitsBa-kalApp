// FIXED USING GOOGLE AI
package com.example.kalapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DispatchedBlue,     // Changed from Purple80
    secondary = UnknownGray,      // Changed from PurpleGrey80
    tertiary = UrgentRed,         // Changed from Pink80
    background = BackgroundDark,
    surface = BackgroundDark,
    onPrimary = SurfaceWhite,
    onBackground = SurfaceWhite,
    onSurface = SurfaceWhite
)

private val LightColorScheme = lightColorScheme(
    primary = DispatchedBlue,     // Changed from Purple40
    secondary = UnknownGray,      // Changed from PurpleGrey40
    tertiary = UrgentRed,         // Changed from Pink40
    background = BackgroundLight,
    surface = SurfaceWhite,
    onPrimary = SurfaceWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun KalAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false to force your custom colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Ensure Typography is defined in Type.kt
        content = content
    )
}
