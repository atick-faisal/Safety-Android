package dev.atick.safety.ui.content.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.data.common.FallIncident
import dev.atick.safety.data.common.FallSeverity
import dev.atick.safety.ui.common.components.NotificationCard

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Notifications", fontSize = 24.sp)
        Text(text = "Unread", fontSize = 18.sp)
        NotificationCard(fallIncident = FallIncident("Brother Nawaf"))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Read", fontSize = 18.sp)
        NotificationCard(fallIncident = FallIncident("Fatima"))
        NotificationCard(fallIncident = FallIncident("You"))
        NotificationCard(
            fallIncident = FallIncident(
                "Brother Nawaf",
                fallSeverity = FallSeverity.Severe
            )
        )
    }
}