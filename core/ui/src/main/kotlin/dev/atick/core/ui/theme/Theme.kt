package dev.atick.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat.getInsetsController
import dagger.hilt.android.internal.managers.FragmentComponentManager

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = BackgroundLight,
    surface = SurfaceLight,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onPrimary = Color.White,
    onSecondary = Color.White
)


private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = BackgroundDark,
    surface = SurfaceDark,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onPrimary = Color.White,
    onSecondary = Color.White
)

@Composable
fun JetpackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        // ... ViewCompat.getWindowInsetsController is deprecated
        // ... https://stackoverflow.com/a/73271312/12737399
        // ... FragmentContextWrapper cannot be cast to android.app.Activity
        // ... https://stackoverflow.com/a/65023375/12737399
        val currentWindow = (FragmentComponentManager.findActivity(view.context) as Activity).window
        SideEffect {
            currentWindow?.let { window ->
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.surface.toArgb()
                getInsetsController(window, view).isAppearanceLightStatusBars = true
                getInsetsController(window, view).isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}