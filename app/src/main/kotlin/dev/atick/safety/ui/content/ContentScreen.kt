package dev.atick.safety.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.atick.safety.data.common.FallIncident
import dev.atick.safety.ui.content.contacts.ContactsScreen
import dev.atick.safety.ui.content.contacts.components.AddContactDialog
import dev.atick.safety.ui.content.devices.DevicesScreen
import dev.atick.safety.ui.content.devices.components.AddDeviceDialog
import dev.atick.safety.ui.content.home.HomeScreen
import dev.atick.safety.ui.content.home.components.ContactSelectionDialog
import dev.atick.safety.ui.content.notifications.NotificationScreen
import dev.atick.safety.ui.content.notifications.components.NotificationDialog
import dev.atick.safety.ui.content.state.ScreenName
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ContentScreen(
    contentViewModel: ContentViewModel = viewModel()
) {
    val contentUiState by contentViewModel.contentUiState.collectAsState()
    val snackbarHost = remember { SnackbarHostState() }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    contentUiState.toastMessage?.let {
        val errorMessage = it.asString()
        LaunchedEffect(contentUiState) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    snackbarHost.showSnackbar(errorMessage)
                    contentViewModel.clearToastMessage()
                }
            }
        }
    }

    var openDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(8.dp)
                    .background(Color.White),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { contentViewModel.setCurrentScreen(ScreenName.Home) }) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = "home")
                }
                IconButton(onClick = { contentViewModel.setCurrentScreen(ScreenName.Notifications) }) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "notification"
                    )
                }
                IconButton(onClick = { contentViewModel.setCurrentScreen(ScreenName.Contacts) }) {
                    Icon(imageVector = Icons.Default.Contacts, contentDescription = "contacts")
                }
                IconButton(onClick = { contentViewModel.setCurrentScreen(ScreenName.Devices) }) {
                    Icon(imageVector = Icons.Default.Watch, contentDescription = "device")
                }
            }
        },
        floatingActionButton = {
            when (contentUiState.currentScreen) {
                ScreenName.Home -> {}
                ScreenName.Notifications -> {}
                ScreenName.Contacts -> {
                    FloatingActionButton(onClick = { openDialog = true }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                    }
                }
                ScreenName.Devices -> {
                    FloatingActionButton(onClick = { openDialog = true }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHost) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (contentUiState.currentScreen) {
                ScreenName.Home -> {
                    HomeScreen(
                        onAlarmClick = { openDialog = true },
                        modifier = Modifier
                            .background(Color.White)
                            .padding(32.dp)
                    )
                    if (openDialog) {
                        ContactSelectionDialog(
                            contacts = contentUiState.contacts,
                            onContactSelected = { contentViewModel.updateContact(it) },
                            onConfirm = { openDialog = false },
                            onDismiss = { openDialog = false }
                        )
                    }
                }
                ScreenName.Notifications -> {
                    NotificationScreen(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(32.dp)
                    )
                    if (openDialog) {
                        NotificationDialog(
                            fallIncident = FallIncident("Brother Nawaf"),
                            onConfirm = { openDialog = false },
                            onDismiss = { openDialog = false }
                        )
                    }
                }
                ScreenName.Contacts -> {
                    ContactsScreen(
                        contacts = contentUiState.contacts,
                        onDeleteClick = { contentViewModel.deleteContact(it) },
                        modifier = Modifier
                            .background(Color.White)
                            .padding(32.dp)
                    )
                    if (openDialog) {
                        AddContactDialog(
                            onConfirm = {
                                contentViewModel.insertContact(it)
                                openDialog = false
                            },
                            onDismiss = { openDialog = false }
                        )
                    }
                }
                ScreenName.Devices -> {
                    DevicesScreen(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(32.dp)
                    )
                    if (openDialog) {
                        AddDeviceDialog(
                            devices = listOf(),
                            onDeviceClick = { openDialog = false },
                            onDismiss = { openDialog = false }
                        )
                    }
                }
            }
        }
    }
}