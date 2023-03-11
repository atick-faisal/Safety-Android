package dev.atick.safety.ui.auth.login.state

import dev.atick.core.ui.base.BaseUiState
import dev.atick.core.ui.utils.UiText

data class LoginUiState(
    override val toastMessage: UiText? = null,
    override val loading: Boolean = false,
    val email: String? = null,
    val password: String? = null
) : BaseUiState()
