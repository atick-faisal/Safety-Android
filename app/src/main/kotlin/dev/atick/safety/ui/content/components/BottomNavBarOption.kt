package dev.atick.safety.ui.content.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.atick.safety.R
import dev.atick.safety.ui.content.state.ScreenName

@Composable
fun BottomNavBarOption(
    screenName: ScreenName,
    currentScreenName: ScreenName,
    showNotificationDot: Boolean,
    onClick: (ScreenName) -> Unit
) {
    IconButton(
        onClick = { onClick(screenName) }
    ) {
        if (screenName == currentScreenName) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ),
            )
        }
        Icon(
            imageVector = when (screenName) {
                ScreenName.Home -> Icons.Outlined.Home
                ScreenName.Notifications -> Icons.Outlined.Notifications
                ScreenName.Contacts -> Icons.Outlined.Person
                ScreenName.Devices -> Icons.Outlined.Watch
            },
            contentDescription = "$screenName"
        )
        if (showNotificationDot) {
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = stringResource(R.string.notification_dot),
                modifier = Modifier
                    .padding(bottom = 16.dp, start = 16.dp)
                    .size(8.dp),
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}