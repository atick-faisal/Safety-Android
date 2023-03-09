package dev.atick.safety.ui.content.devices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.atick.safety.data.devices.SafetyDevice
import dev.atick.safety.ui.content.devices.components.DeviceCard

@Composable
fun AddDeviceDialog(
    onDeviceClick: (SafetyDevice) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Pair Device")
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DeviceCard(safetyDevice = SafetyDevice("", "ESP32"), onClick = onDeviceClick)
                DeviceCard(safetyDevice = SafetyDevice("", "ESP32"), onClick = onDeviceClick)
                DeviceCard(safetyDevice = SafetyDevice("", "ESP32"), onClick = onDeviceClick)
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(text = "Cancel")
            }
        }
    )
}