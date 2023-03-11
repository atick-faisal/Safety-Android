package dev.atick.safety.ui.auth.login

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.internal.managers.FragmentComponentManager
import dev.atick.safety.R
import dev.atick.safety.ui.common.components.LargeButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel()
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
                window.statusBarColor = Color.White.toArgb()
                window.navigationBarColor = Color.White.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                    true
            }
        }
    }

    val loginUiState by loginViewModel.loginUiState.collectAsState()
    val snackbarHost = remember { SnackbarHostState() }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    loginUiState.toastMessage?.let {
        val errorMessage = it.asString()
        LaunchedEffect(loginUiState) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    snackbarHost.showSnackbar(errorMessage)
                    loginViewModel.clearToastMessage()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHost) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(32.dp)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .align(CenterHorizontally),
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "icon"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Login",
                fontSize = 48.sp,
                // fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Email", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = loginUiState.email ?: "",
                onValueChange = { loginViewModel.setEmail(it) },
                placeholder = { Text(text = "e.g. someone@mail.com", color = Color(0x40_000000)) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Password", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = loginUiState.password ?: "",
                onValueChange = { loginViewModel.setPassword(it) },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.weight(1F))

            LargeButton(
                text = "Login",
                onClick = { loginViewModel.login() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}