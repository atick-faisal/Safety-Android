package dev.atick.safety.ui.content.devices.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.atick.safety.R
import dev.atick.safety.data.devices.SafetyDevice

@Composable
fun AddDeviceDialog(
    devices: List<SafetyDevice>,
    onDeviceClick: (SafetyDevice) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.pair_device))
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(devices) { device ->
                    DeviceCard(
                        safetyDevice = device,
                        onClick = onDeviceClick
                    )
                }
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
                Text(text = stringResource(id = R.string.cancel))
            }
        }
    )
}