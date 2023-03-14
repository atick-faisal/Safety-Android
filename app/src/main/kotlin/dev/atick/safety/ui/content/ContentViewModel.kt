package dev.atick.safety.ui.content

import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    smsDataSource: SmsDataSource,
    private val contentRepository: ContentRepository
) : BaseViewModel<ContentUiState>() {

    private val _contentUiState = MutableStateFlow(ContentUiState())
    val contentUiState = combineAll(
        _contentUiState,
        contentRepository.getContacts(),
        contentRepository.getRecentFallIncident(),
        contentRepository.getUnreadFallIncidents(),
        contentRepository.getReadFallIncidents(),
        contentRepository.getPairedDevices(),
        contentRepository.getScannedDevices()
    ) { contentUiState,
        contacts,
        recentFallIncident,
        unreadFallIncidents,
        readFallIncidents,
        pairedDevices,
        scannedDevices ->
        contentUiState.copy(
            contacts = contacts,
            recentFallIncident = recentFallIncident,
            unreadFallIncidents = unreadFallIncidents,
            readFallIncidents = readFallIncidents,
            pairedDevices = pairedDevices,
            scannedDevices = scannedDevices
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

    fun startDiscovery() {
        Logger.d("SCAN STARTED")
        val result = contentRepository.startDiscovery()
        if (result.isSuccess) {
            _contentUiState.update {
                it.copy(toastMessage = UiText.DynamicString("Scan Started!"))
            }
        } else {
            _contentUiState.update {
                it.copy(toastMessage = UiText.DynamicString("${result.exceptionOrNull()}"))
            }
        }
    }

    fun stopDiscovery() {
        val result = contentRepository.stopDiscovery()
        if (result.isSuccess) {
            _contentUiState.update {
                it.copy(toastMessage = UiText.DynamicString("Scan Stopped!"))
            }
        } else {
            _contentUiState.update {
                it.copy(toastMessage = UiText.DynamicString("${result.exceptionOrNull()}"))
            }
        }
    }

    fun clearToastMessage() {
        _contentUiState.update { it.copy(toastMessage = null) }
    }

    private inline fun <T1, T2, T3, T4, T5, T6, T7, R> combineAll(
        flow: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        flow4: Flow<T4>,
        flow5: Flow<T5>,
        flow6: Flow<T6>,
        flow7: Flow<T7>,
        crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R
    ): Flow<R> {
        return combine(
            flow,
            flow2,
            flow3,
            flow4,
            flow5,
            flow6,
            flow7
        ) { args: Array<*> ->
            @Suppress("UNCHECKED_CAST")
            transform(
                args[0] as T1,
                args[1] as T2,
                args[2] as T3,
                args[3] as T4,
                args[4] as T5,
                args[5] as T6,
                args[6] as T7,
            )
        }
    }
}