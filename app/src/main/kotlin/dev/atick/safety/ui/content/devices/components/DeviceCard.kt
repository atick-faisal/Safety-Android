package dev.atick.safety.ui.content.devices.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.data.devices.SafetyDevice

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DeviceCard(
    safetyDevice: SafetyDevice,
    onClick: (SafetyDevice) -> Unit
) {
    val cardColor = remember {
        if (safetyDevice.connected) Color(0xFFFF9292)
        else Color(0xFFCECECE)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
            contentColor = Color(0xFF4E4E4E)
        ),
        onClick = { onClick(safetyDevice) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = safetyDevice.name ?: "Unnamed", fontSize = 18.sp)
            Icon(imageVector = Icons.Default.Bluetooth, contentDescription = "connect")
        }
    }
}