package dev.atick.safety.ui.content.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.data.common.FallIncident
import dev.atick.safety.ui.common.components.NotificationCard

@Composable
fun NotificationScreen(
    unreadFallIncidents: List<FallIncident>,
    readFallIncidents: List<FallIncident>,
    onNotificationClick: (FallIncident) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Text(text = "Notifications", fontSize = 24.sp) }
        item { Text(text = "Unread", fontSize = 18.sp) }
        items(unreadFallIncidents) { fallIncident ->
            NotificationCard(fallIncident = fallIncident, onClick = onNotificationClick)
        }
        item { Divider() }
        item { Text(text = "Read", fontSize = 18.sp) }
        items(readFallIncidents) { fallIncident ->
            NotificationCard(fallIncident = fallIncident, onClick = onNotificationClick)
        }
    }
}