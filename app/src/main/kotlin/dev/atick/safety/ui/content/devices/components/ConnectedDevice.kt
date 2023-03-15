package dev.atick.safety.ui.content.devices.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.R
import dev.atick.safety.ui.common.components.LargeButton

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
                imageVector = Icons.Filled.Check,
                tint = Color.Green,
                contentDescription = "Connected",
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.BottomEnd)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        LargeButton(text = "Close Connection", onClick = onCloseConnectionClick)
    }
}