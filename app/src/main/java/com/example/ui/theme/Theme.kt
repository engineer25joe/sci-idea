package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color(0xFF3D0000),

    primaryContainer = SciRedDark,
    onPrimaryContainer = Color.White,

    secondary = DarkSecondary,
    onSecondary = SciBlueDark,

    secondaryContainer = SciBlueDark,
    onSecondaryContainer = Color(0xFFE8ECFF),

    tertiary = KenyaGold,
    onTertiary = Color(0xFF241700),

    background = DarkBackground,
    onBackground = DarkTextPrimary,

    surface = DarkSurface,
    onSurface = DarkTextPrimary,

    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,

    error = SciRedLight,
    onError = Color.White,

    errorContainer = StatusErrorRed,
    onErrorContainer = Color.White
)

private val LightColorScheme = lightColorScheme(
    // SCI Red is the primary action colour
    primary = SciRedAccent,
    onPrimary = Color.White,

    primaryContainer = SciRedContainer,
    onPrimaryContainer = OnSciRedContainer,

    // SCI Navy is the secondary brand colour
    secondary = SciBluePrimary,
    onSecondary = Color.White,

    secondaryContainer = SciBlueContainer,
    onSecondaryContainer = OnSciBlueContainer,

    // Gold is reserved for highlights
    tertiary = KenyaGoldDark,
    onTertiary = Color.White,

    background = SurfaceCream,
    onBackground = TextPrimary,

    surface = SurfaceCard,
    onSurface = TextPrimary,

    surfaceVariant = SurfaceVariantWarm,
    onSurfaceVariant = TextSecondary,

    error = StatusErrorRed,
    onError = Color.White,

    errorContainer = StatusErrorContainer,
    onErrorContainer = OnSciRedContainer
)

@Composable
fun BiasharaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current

            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorScheme

        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}