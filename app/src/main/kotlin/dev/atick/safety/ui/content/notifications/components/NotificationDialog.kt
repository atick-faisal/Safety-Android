package dev.atick.safety.ui.content.notifications.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.R
import dev.atick.safety.data.common.FallIncident

@Composable
fun NotificationDialog(
    fallIncident: FallIncident,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = "alert",
                    Modifier.size(120.dp)
                )
                Text(
                    text = "${fallIncident.victimName} Fell Down!",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                Text(
                    text = "A fall as been detected for " +
                        "${fallIncident.victimName} at " +
                        "${fallIncident.getFormattedTime()} on " +
                        fallIncident.getFormattedDate(),
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Ok")
            }
        }
    )
}