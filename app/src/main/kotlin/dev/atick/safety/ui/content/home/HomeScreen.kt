package dev.atick.safety.ui.content.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.R
import dev.atick.safety.data.common.FallIncident
import dev.atick.safety.ui.common.components.NotificationCard
import dev.atick.safety.ui.content.home.components.DeviceInfoCard
import dev.atick.safety.ui.content.home.components.FallInfoCard

@Composable
fun HomeScreen(
    nFallIncidents: Int,
    isDeviceConnected: Boolean,
    recentFallIncident: FallIncident?,
    onAlarmClick: () -> Unit,
    onSeeAllClick: () -> Unit,
    onDeviceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = stringResource(R.string.app_icon)
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.safety_app),
            fontSize = 48.sp,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FallInfoCard(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight(),
                nFallIncidents = nFallIncidents,
                onClick = onDeviceClick
            )

            DeviceInfoCard(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight(),
                isDeviceConnected = isDeviceConnected,
                onClick = onDeviceClick
            )
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.error,
            onClick = onAlarmClick
        ) {
            Text(
                text = stringResource(R.string.alarm),
                fontSize = 32.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = stringResource(R.string.recent_falls),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        recentFallIncident?.let { NotificationCard(fallIncident = recentFallIncident) {} }

        Text(
            text = stringResource(R.string.see_all),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSeeAllClick() }
        )
    }
}