package dev.atick.safety.ui.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.data.common.FallIncident

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NotificationCard(
    fallIncident: FallIncident,
    onClick: (FallIncident) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (fallIncident.highRisk) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (fallIncident.highRisk) MaterialTheme.colorScheme.onError
            else MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        onClick = { onClick(fallIncident.copy(readByUser = true)) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = fallIncident.victimName, fontSize = 18.sp)
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(text = fallIncident.getFormattedDate())
                Text(text = fallIncident.getFormattedTime())
            }
        }
    }
}