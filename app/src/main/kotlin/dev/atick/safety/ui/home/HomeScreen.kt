package dev.atick.safety.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .shadow(8.dp)
                    .background(Color.White),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = "home")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "notification"
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Contacts, contentDescription = "contacts")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Watch, contentDescription = "device")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(Color.White)
                .padding(32.dp),
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
                        Text(text = "02", fontSize = 56.sp, color = Color.White)
                        Text(
                            text = "Your week’s total falls",
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

            Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFFE373FF), onClick = {}) {
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

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFCECECE))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Brother Nawaf", fontSize = 18.sp, color = Color(0xFF4E4E4E))
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = "25-02-1023", color = Color(0xFF4E4E4E))
                        Text(text = "14:34", color = Color(0xFF4E4E4E))
                    }
                }
            }

            Text(
                text = "See All",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
            )
        }
    }
}