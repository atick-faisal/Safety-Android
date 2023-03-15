package dev.atick.safety.ui.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LargeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: ImageVector? = null,
) {
    Button(
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(28.dp),
        onClick = onClick
    ) {
        Row(
            Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, fontSize = 18.sp)
            trailingIcon?.let {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(imageVector = trailingIcon, contentDescription = text)
            }
        }
    }
}