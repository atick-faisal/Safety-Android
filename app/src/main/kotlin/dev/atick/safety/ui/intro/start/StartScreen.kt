package dev.atick.safety.ui.intro.start

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import dagger.hilt.android.internal.managers.FragmentComponentManager
import dev.atick.safety.R

@Composable
fun StartScreen(
    onNextClick: () -> Unit,
    onBackClick: (() -> Unit)? = null
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        // ... ViewCompat.getWindowInsetsController is deprecated
        // ... https://stackoverflow.com/a/73271312/12737399
        // ... FragmentContextWrapper cannot be cast to android.app.Activity
        // ... https://stackoverflow.com/a/65023375/12737399
        val currentWindow = (FragmentComponentManager.findActivity(view.context) as Activity).window
        SideEffect {
            currentWindow?.let { window ->
                window.statusBarColor = Color.Black.toArgb()
                window.navigationBarColor = Color.Black.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                    false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = "icon",
            modifier = Modifier.size(160.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Safety App\n" +
                "تطبيق السلامة",
            fontSize = 40.sp,
            color = Color.White,
            lineHeight = 56.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Where your safety is our  priority",
            fontSize = 24.sp,
            color = Color.White,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "حيث سلامتك هي أولويتنا القصوى",
            fontSize = 28.sp,
            color = Color.White,
            lineHeight = 36.sp,
            textAlign = TextAlign.Right
        )
    }
}