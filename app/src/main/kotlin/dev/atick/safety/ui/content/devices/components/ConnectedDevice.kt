package dev.atick.safety.ui.content.devices.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.R

@Composable
fun ConnectedDevice(
    onCloseConnectionClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Safety Bracelet is\nConnected",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Box {
            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "Connected"
            )
            Icon(
                imageVector = Icons.Filled.Verified,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Connected",
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.BottomEnd)
            )
        }
        Spacer(modifier = Modifier.height(64.dp))
        Button(
            onClick = onCloseConnectionClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Row(
                Modifier.padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "close")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Close Connection")
            }
        }
    }
}