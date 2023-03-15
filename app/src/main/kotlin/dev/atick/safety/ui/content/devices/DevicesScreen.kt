package dev.atick.safety.ui.content.devices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.R
import dev.atick.safety.data.devices.SafetyDevice
import dev.atick.safety.ui.content.devices.components.ConnectedDevice
import dev.atick.safety.ui.content.devices.components.DeviceCard

@Composable
fun DevicesScreen(
    isDeviceConnected: Boolean,
    pairedDevices: List<SafetyDevice>,
    onDeviceClick: (SafetyDevice) -> Unit,
    onCloseConnectionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isDeviceConnected) {
        ConnectedDevice(onCloseConnectionClick)
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Text(text = stringResource(R.string.paired_devices), fontSize = 24.sp) }
            items(pairedDevices) { safetyDevice ->
                DeviceCard(safetyDevice = safetyDevice, onClick = onDeviceClick)
            }
        }
    }
}