package dev.atick.safety.ui.content.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.R
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
        item { Text(text = stringResource(R.string.notifications), fontSize = 24.sp) }
        item { Text(text = stringResource(R.string.unread), fontSize = 18.sp) }
        if (unreadFallIncidents.isEmpty()) {
            item { Text(text = stringResource(id = R.string.nothing_to_show)) }
        }
        items(unreadFallIncidents) { fallIncident ->
            NotificationCard(fallIncident = fallIncident, onClick = onNotificationClick)
        }
        item { Divider() }
        item { Text(text = stringResource(R.string.read), fontSize = 18.sp) }
        if (readFallIncidents.isEmpty()) {
            item { Text(text = stringResource(id = R.string.nothing_to_show)) }
        }
        items(readFallIncidents) { fallIncident ->
            NotificationCard(fallIncident = fallIncident, onClick = onNotificationClick)
        }
    }
}