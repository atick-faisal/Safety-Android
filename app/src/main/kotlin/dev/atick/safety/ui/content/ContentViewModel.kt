package dev.atick.safety.ui.content

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.extensions.stateInDelayed
import dev.atick.core.ui.base.BaseViewModel
import dev.atick.core.ui.utils.UiText
import dev.atick.safety.data.common.FallIncident
import dev.atick.safety.data.contacts.Contact
import dev.atick.safety.repository.content.ContentRepository
import dev.atick.safety.ui.content.state.ContentUiState
import dev.atick.safety.ui.content.state.ScreenName
import dev.atick.sms.data.SmsDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    smsDataSource: SmsDataSource,
    private val contentRepository: ContentRepository
) : BaseViewModel<ContentUiState>() {

    private val _contentUiState = MutableStateFlow(ContentUiState())
    val contentUiState = combine(
        _contentUiState,
        contentRepository.getContacts(),
        contentRepository.getRecentFallIncident(),
        contentRepository.getUnreadFallIncidents(),
        contentRepository.getReadFallIncidents()
    ) { contentUiState, contacts, recentFallIncident, unreadFallIncidents, readFallIncidents ->
        contentUiState.copy(
            contacts = contacts,
            recentFallIncident = recentFallIncident,
            unreadFallIncidents = unreadFallIncidents,
            readFallIncidents = readFallIncidents
        )
    }.stateInDelayed(ContentUiState(), viewModelScope)

    init {
        viewModelScope.launch {
            smsDataSource.syncEmergencyMessages()
        }
    }

    fun setCurrentScreen(currentScreen: ScreenName) {
        _contentUiState.update { it.copy(currentScreen = currentScreen) }
    }

    fun insertContact(contact: Contact) {
        viewModelScope.launch {
            val result = contentRepository.insertContact(contact)
            if (result.isSuccess) {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("Contact Added!"))
                }
            } else {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("${result.exceptionOrNull()}"))
                }
            }
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            val result = contentRepository.updateContact(contact)
            if (result.isSuccess) {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("Contact Updated!"))
                }
            } else {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("${result.exceptionOrNull()}"))
                }
            }
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            val result = contentRepository.deleteContact(contact)
            if (result.isSuccess) {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("Contact Deleted!"))
                }
            } else {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("${result.exceptionOrNull()}"))
                }
            }
        }
    }

    private fun insertFallIncident(fallIncident: FallIncident) {
        viewModelScope.launch {
            val result = contentRepository.insertFallIncident(fallIncident)
            if (result.isSuccess) {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("Fall Added!"))
                }
            } else {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("${result.exceptionOrNull()}"))
                }
            }
        }
    }

    fun updateFallIncident(fallIncident: FallIncident) {
        viewModelScope.launch {
            val result = contentRepository.updateFallIncident(fallIncident)
            if (result.isSuccess) {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("Incident Marked As Read!"))
                }
            } else {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("${result.exceptionOrNull()}"))
                }
            }
        }
    }

    fun clearToastMessage() {
        _contentUiState.update { it.copy(toastMessage = null) }
    }
}