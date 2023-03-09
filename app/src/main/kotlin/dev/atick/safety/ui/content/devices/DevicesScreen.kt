package dev.atick.safety.ui.content.devices

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.data.devices.SafetyDevice
import dev.atick.safety.ui.content.devices.components.DeviceCard

@Composable
fun DevicesScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Paired Devices", fontSize = 24.sp)
        DeviceCard(safetyDevice = SafetyDevice("", "ESP32"), onClick = { })
        DeviceCard(safetyDevice = SafetyDevice("", "ESP32"), onClick = { })
        DeviceCard(safetyDevice = SafetyDevice("", "ESP32"), onClick = { })
        DeviceCard(safetyDevice = SafetyDevice("", "ESP32"), onClick = { })
    }
}