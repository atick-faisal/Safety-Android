package dev.atick.safety.ui.content.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DeviceInfoCard(
    modifier: Modifier = Modifier,
    isDeviceConnected: Boolean,
    onDeviceClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        color = if (isDeviceConnected) Color(0xFF54C1FF) else MaterialTheme.colorScheme.error,
        shape = RoundedCornerShape(16.dp),
        onClick = onDeviceClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (isDeviceConnected) Icons.Filled.Verified else Icons.Default.Cancel,
                contentDescription = "connection",
                Modifier
                    .size(64.dp)
                    .weight(1F),
                tint = Color.White
            )
            Text(
                text = if (isDeviceConnected) "Safety Bracelet Connected" else "Safety Bracelet Disconnected",
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}