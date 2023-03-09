package dev.atick.safety.ui.content.contacts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.data.contacts.Contact
import dev.atick.safety.ui.content.contacts.components.ContactCard

@Composable
fun ContactsScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Contacts", fontSize = 24.sp)
        ContactCard(contact = Contact("Brother Nawaf", "")) { }
        ContactCard(contact = Contact("Mom", "")) { }
        ContactCard(contact = Contact("Dad", "")) { }
        ContactCard(contact = Contact("Dr. Hussain", "")) { }
    }
}