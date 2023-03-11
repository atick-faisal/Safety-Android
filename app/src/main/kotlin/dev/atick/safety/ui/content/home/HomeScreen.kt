package dev.atick.safety.ui.content.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.R
import dev.atick.safety.data.common.FallIncident
import dev.atick.safety.ui.common.components.NotificationCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    nFallIncidents: Int,
    recentFallIncident: FallIncident?,
    onAlarmClick: () -> Unit,
    onSeeAllClick: () -> Unit,
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
            contentDescription = "icon"
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Safety App",
            fontSize = 48.sp,
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight(),
                color = Color(0xFFFF7D7D),
                shape = RoundedCornerShape(16.dp),
                onClick = {}
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "$nFallIncidents", fontSize = 56.sp, color = Color.White)
                    Text(
                        text = "New Falls This Week",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight(),
                color = Color(0xFF54C1FF),
                shape = RoundedCornerShape(16.dp),
                onClick = {}
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.TaskAlt,
                        contentDescription = "connection",
                        Modifier
                            .size(64.dp)
                            .weight(1F),
                        tint = Color.White
                    )
                    Text(
                        text = "Safety bracelet Connected",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFE373FF),
            onClick = onAlarmClick
        ) {
            Text(
                text = "Alarm",
                fontSize = 32.sp,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }

        Text(text = "Recent Falls", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

        recentFallIncident?.let { NotificationCard(fallIncident = recentFallIncident) {} }

        Text(
            text = "See All",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSeeAllClick() }
        )
    }
}