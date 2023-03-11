package dev.atick.safety.ui.intro.page3

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import dev.atick.safety.ui.common.components.LargeButton

@Composable
fun IntroScreen3(
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
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
                window.statusBarColor = Color(0xFF6C6C6C).toArgb()
                window.navigationBarColor = Color.White.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                    true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6C6C6C)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.intro_page_3_header),
            contentDescription = "icon",
            modifier = Modifier
                .size(200.dp)
                .weight(0.5F)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 32.dp)
                .weight(0.5F),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Add your family members to your contact list to notify them in case of any sudden falls",
                fontSize = 24.sp,
                color = Color.Black,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "أضف أفراد عائلتك إلى قائمة جهات الاتصال الخاصة بك لإخطارهم في حالة حدوث أي سقوط مفاجئ",
                fontSize = 28.sp,
                color = Color.Black,
                lineHeight = 36.sp,
                textAlign = TextAlign.Right
            )

            Spacer(modifier = Modifier.weight(1.0F))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                }
                LargeButton(
                    text = "Next",
                    onClick = onNextClick,
                    trailingIcon = Icons.Default.ArrowForward
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}