package dev.atick.safety.ui.content.contacts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.safety.R
import dev.atick.safety.data.contacts.Contact
import dev.atick.safety.ui.content.contacts.components.ContactCard

@Composable
fun ContactsScreen(
    contacts: List<Contact>,
    onDeleteClick: (Contact) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(R.string.contacts), fontSize = 24.sp)
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(contacts) { contact ->
                ContactCard(contact = contact, onDeleteClick = onDeleteClick)
            }
        }
    }
}