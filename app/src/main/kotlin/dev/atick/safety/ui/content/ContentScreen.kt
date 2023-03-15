package dev.atick.safety.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Watch
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
import dev.atick.safety.data.devices.SafetyDevice
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
    onDeviceClick: (SafetyDevice) -> Unit,
    onCloseConnectionClick: () -> Unit,
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
    var openNotificationDialog by remember { mutableStateOf<FallIncident?>(null) }

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
                    Icon(imageVector = Icons.Outlined.Home, contentDescription = "home")
                }
                IconButton(
                    onClick = { contentViewModel.setCurrentScreen(ScreenName.Notifications) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "notification"
                    )
                    if (contentUiState.unreadFallIncidents.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Circle,
                            contentDescription = "dot",
                            modifier = Modifier
                                .padding(bottom = 16.dp, start = 16.dp)
                                .size(8.dp),
                            tint = Color.Red
                        )
                    }
                }
                IconButton(onClick = { contentViewModel.setCurrentScreen(ScreenName.Contacts) }) {
                    Icon(imageVector = Icons.Outlined.Contacts, contentDescription = "contacts")
                }
                IconButton(onClick = { contentViewModel.setCurrentScreen(ScreenName.Devices) }) {
                    Icon(imageVector = Icons.Outlined.Watch, contentDescription = "device")
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
                    if (!contentUiState.connectedDevice.connected) {
                        FloatingActionButton(onClick = {
                            contentViewModel.startDiscovery()
                            openDialog = true
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "add")
                        }
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
                        nFallIncidents = contentUiState.unreadFallIncidents.size,
                        isDeviceConnected = contentUiState.connectedDevice.connected,
                        recentFallIncident = contentUiState.recentFallIncident,
                        onAlarmClick = { openDialog = true },
                        onSeeAllClick = {
                            contentViewModel.setCurrentScreen(ScreenName.Notifications)
                        },
                        onDeviceClick = {
                            contentViewModel.setCurrentScreen(ScreenName.Devices)
                        },
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
                        readFallIncidents = contentUiState.readFallIncidents,
                        unreadFallIncidents = contentUiState.unreadFallIncidents,
                        onNotificationClick = {
                            openNotificationDialog = it
                            contentViewModel.updateFallIncident(it)
                        },
                        modifier = Modifier
                            .background(Color.White)
                            .padding(32.dp)
                    )
                    openNotificationDialog?.let {
                        NotificationDialog(
                            fallIncident = it,
                            onConfirm = { openNotificationDialog = null },
                            onDismiss = { openNotificationDialog = null }
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
                        isDeviceConnected = contentUiState.connectedDevice.connected,
                        pairedDevices = contentUiState.pairedDevices,
                        onDeviceClick = onDeviceClick,
                        onCloseConnectionClick = {
                            contentViewModel.closeConnection()
                            onCloseConnectionClick()
                        },
                        modifier = Modifier
                            .background(Color.White)
                            .padding(32.dp)
                    )
                    if (openDialog) {
                        AddDeviceDialog(
                            devices = contentUiState.scannedDevices,
                            onDeviceClick = {
                                onDeviceClick(it)
                                contentViewModel.stopDiscovery()
                                openDialog = false
                            },
                            onDismiss = {
                                contentViewModel.stopDiscovery()
                                openDialog = false
                            }
                        )
                    }
                }
            }
        }
    }
}