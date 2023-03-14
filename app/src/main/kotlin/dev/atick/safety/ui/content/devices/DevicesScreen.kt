package dev.atick.safety.ui.content.devices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.data.devices.SafetyDevice
import dev.atick.safety.ui.content.devices.components.DeviceCard

@Composable
fun DevicesScreen(
    pairedDevices: List<SafetyDevice>,
    onDeviceClick: (SafetyDevice) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Text(text = "Paired Devices", fontSize = 24.sp) }
        items(pairedDevices) { safetyDevice ->
            DeviceCard(safetyDevice = safetyDevice, onClick = onDeviceClick)
        }
    }
}