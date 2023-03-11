package dev.atick.safety.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.core.data.SingleLiveEvent
import dev.atick.core.extensions.isValidEmail
import dev.atick.core.ui.base.BaseViewModel
import dev.atick.core.ui.utils.UiText
import dev.atick.safety.repository.auth.AuthRepository
import dev.atick.safety.ui.auth.login.state.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel<LoginUiState>() {

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private val _userId = MutableLiveData<SingleLiveEvent<String>>()
    val userId: LiveData<SingleLiveEvent<String>>
        get() = _userId

    fun login() {
        Logger.d("LOGIN ... ")
        if (loginUiState.value.email.isValidEmail() && !loginUiState.value.password.isNullOrEmpty()) {
            viewModelScope.launch {
                val result = authRepository.saveUserId("USER_ID")
                if (result.isSuccess) {
                    _userId.postValue(SingleLiveEvent("USER_ID"))
                }
            }
        } else {
            _loginUiState.update {
                it.copy(toastMessage = UiText.DynamicString("Some Fields are Invalid!"))
            }
        }
    }

    fun setEmail(email: String) {
        _loginUiState.update { it.copy(email = email) }
    }

    fun setPassword(password: String) {
        _loginUiState.update { it.copy(password = password) }
    }

    fun clearToastMessage() {
        _loginUiState.update { it.copy(toastMessage = null) }
    }
}
