package dev.atick.safety.ui.content.state

import dev.atick.core.ui.base.BaseUiState
import dev.atick.core.ui.utils.UiText
import dev.atick.safety.data.contacts.Contact

data class ContentUiState(
    override val loading: Boolean = false,
    override val toastMessage: UiText? = null,
    val currentScreen: ScreenName = ScreenName.Home,
    val contacts: List<Contact> = listOf()
) : BaseUiState()

sealed interface ScreenName {
    object Home : ScreenName
    object Notifications : ScreenName
    object Contacts : ScreenName
    object Devices : ScreenName
}
