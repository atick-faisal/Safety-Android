package dev.atick.safety.ui.content

import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.extensions.stateInDelayed
import dev.atick.core.ui.base.BaseViewModel
import dev.atick.core.ui.utils.UiText
import dev.atick.safety.R
import dev.atick.safety.data.common.FallIncident
import dev.atick.safety.data.contacts.Contact
import dev.atick.safety.repository.content.ContentRepository
import dev.atick.safety.ui.content.state.ContentUiState
import dev.atick.safety.ui.content.state.ScreenName
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
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
        contentRepository.getScannedDevices(),
        contentRepository.getConnectedDevice()
    ) { contentUiState,
        contacts,
        recentFallIncident,
        unreadFallIncidents,
        readFallIncidents,
        pairedDevices,
        scannedDevices,
        connectedDevice ->
        contentUiState.copy(
            contacts = contacts,
            recentFallIncident = recentFallIncident,
            unreadFallIncidents = unreadFallIncidents,
            readFallIncidents = readFallIncidents,
            pairedDevices = pairedDevices,
            scannedDevices = scannedDevices,
            connectedDevice = connectedDevice
        )
    }.stateInDelayed(ContentUiState(), viewModelScope)

    private var syncSmsJob: Job? = null
    private var sendSmsJob: Job? = null
    private var insertContactJob: Job? = null
    private var updateContactJob: Job? = null
    private var deleteContactJob: Job? = null
    private var updateFallJob: Job? = null

    init {
        if (syncSmsJob == null) {
            syncSmsJob = viewModelScope.launch {
                val result = contentRepository.syncEmergencyMessages()
                if (result.isFailure) {
                    _contentUiState.update {
                        it.copy(
                            toastMessage = UiText.DynamicString("${result.exceptionOrNull()}")
                        )
                    }
                }
                syncSmsJob = null
            }
        }
    }

    fun setCurrentScreen(currentScreen: ScreenName) {
        _contentUiState.update { it.copy(currentScreen = currentScreen) }
    }

    fun insertContact(contact: Contact) {
        if (insertContactJob != null) return
        insertContactJob = viewModelScope.launch {
            val result = contentRepository.insertContact(contact)
            if (result.isSuccess) {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.StringResource(R.string.contact_added))
                }
            } else {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("${result.exceptionOrNull()}"))
                }
            }
            insertContactJob = null
        }
    }

    fun updateContact(contact: Contact) {
        if (updateContactJob != null) return
        updateContactJob = viewModelScope.launch {
            val result = contentRepository.updateContact(contact)
            if (result.isSuccess) {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.StringResource(R.string.marked_emergency))
                }
            } else {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("${result.exceptionOrNull()}"))
                }
            }
            updateContactJob = null
        }
    }

    fun deleteContact(contact: Contact) {
        if (deleteContactJob != null) return
        viewModelScope.launch {
            val result = contentRepository.deleteContact(contact)
            if (result.isSuccess) {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.StringResource(R.string.contact_deleted))
                }
            } else {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("${result.exceptionOrNull()}"))
                }
            }
            deleteContactJob = null
        }
    }

    fun updateFallIncident(fallIncident: FallIncident) {
        if (updateFallJob != null) return
        updateFallJob = viewModelScope.launch {
            val result = contentRepository.updateFallIncident(fallIncident)
            if (result.isSuccess) {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.StringResource(R.string.fall_marked_read))
                }
            } else {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("${result.exceptionOrNull()}"))
                }
            }
            updateFallJob = null
        }
    }

    fun startDiscovery() {
        Logger.d("SCAN STARTED")
        val result = contentRepository.startDiscovery()
        if (result.isSuccess) {
            _contentUiState.update {
                it.copy(toastMessage = UiText.StringResource(R.string.scan_started))
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
                it.copy(toastMessage = UiText.StringResource(R.string.scan_stopped))
            }
        } else {
            _contentUiState.update {
                it.copy(toastMessage = UiText.DynamicString("${result.exceptionOrNull()}"))
            }
        }
    }

    fun closeConnection() {
        val result = contentRepository.closeConnection()
        if (result.isSuccess) {
            _contentUiState.update {
                it.copy(toastMessage = UiText.StringResource(R.string.closing_connection))
            }
        } else {
            _contentUiState.update {
                it.copy(toastMessage = UiText.DynamicString("${result.exceptionOrNull()}"))
            }
        }
    }

    fun sendEmergencySmsToSelectedContacts() {
        if (sendSmsJob != null) return
        sendSmsJob = viewModelScope.launch {
            val result = contentRepository.sendEmergencySmsToSelectedContacts()
            if (result.isSuccess) {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.StringResource(R.string.emergency_sms_sent))
                }
            } else {
                _contentUiState.update {
                    it.copy(toastMessage = UiText.DynamicString("${result.exceptionOrNull()}"))
                }
            }
            sendSmsJob = null
        }
    }

    fun clearToastMessage() {
        _contentUiState.update { it.copy(toastMessage = null) }
    }

    // ------------------ Flow Combine Extension ---------------------------

    private inline fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combineAll(
        flow: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        flow4: Flow<T4>,
        flow5: Flow<T5>,
        flow6: Flow<T6>,
        flow7: Flow<T7>,
        flow8: Flow<T8>,
        crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8) -> R
    ): Flow<R> {
        return combine(
            flow,
            flow2,
            flow3,
            flow4,
            flow5,
            flow6,
            flow7,
            flow8
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
                args[7] as T8,
            )
        }
    }
}