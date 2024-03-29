package dev.atick.safety.ui.content.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.outlined.GppBad
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.atick.safety.R

@Composable
fun DeviceInfoCard(
    modifier: Modifier = Modifier,
    isDeviceConnected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        color = if (isDeviceConnected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.tertiaryContainer,
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (isDeviceConnected) Icons.Filled.Verified
                else Icons.Outlined.GppBad,
                contentDescription = stringResource(R.string.connection),
                Modifier
                    .size(64.dp)
                    .weight(1F),
            )
            Text(
                text = if (isDeviceConnected) stringResource(R.string.safety_bracelet_connected)
                else stringResource(R.string.safety_bracelet_disconnected),
                textAlign = TextAlign.Center
            )
        }
    }
}