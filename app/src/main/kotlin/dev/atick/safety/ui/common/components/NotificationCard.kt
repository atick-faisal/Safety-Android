package dev.atick.safety.ui.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.data.common.FallIncident
import dev.atick.safety.data.common.FallSeverity

@Composable
fun NotificationCard(
    fallIncident: FallIncident
) {
    val cardColor = remember {
        if (fallIncident.fallSeverity == FallSeverity.Severe) Color(0xFFFF9292)
        else Color(0xFFCECECE)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
            contentColor = Color(0xFF4E4E4E)
        )
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