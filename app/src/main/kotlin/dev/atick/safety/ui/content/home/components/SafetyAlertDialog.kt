package dev.atick.safety.ui.content.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.R
import dev.atick.safety.data.contacts.Contact

@Composable
fun SafetyAlertDialog(
    contacts: List<Contact>,
    onContactSelected: (Contact) -> Unit,
    onPlayAlarmSoundClick: (Boolean) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var isAlarmPlaying by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            isAlarmPlaying = !isAlarmPlaying
                            onPlayAlarmSoundClick(isAlarmPlaying)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAlarmPlaying) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary,
                            contentColor = if (isAlarmPlaying) MaterialTheme.colorScheme.onError
                            else MaterialTheme.colorScheme.onPrimary,
                        )
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isAlarmPlaying) Icons.Default.Close
                                else Icons.Outlined.Notifications,
                                contentDescription = stringResource(R.string.toggle_alarm)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = if (isAlarmPlaying) "STOP ALARM SOUND" else "PLAY ALARM SOUND")
                        }
                    }
                }
                item { Divider() }
                item { Text(text = stringResource(R.string.select_contacts), fontSize = 18.sp) }
                if (contacts.isEmpty())
                    item { Text(text = stringResource(R.string.no_contact_found)) }
                items(contacts) { contact ->
                    ContactCard(contact = contact, onSelect = onContactSelected)
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = stringResource(R.string.send))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}