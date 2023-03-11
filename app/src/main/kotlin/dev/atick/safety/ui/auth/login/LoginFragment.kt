package dev.atick.safety.ui.auth.login

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.extensions.observeEvent
import dev.atick.core.ui.base.BaseFragment
import dev.atick.core.ui.theme.JetpackTheme

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private val loginViewModel: LoginViewModel by viewModels()

    @Composable
    override fun ComposeUi() {
        JetpackTheme {
            LoginScreen()
        }
    }

    override fun observeStates() {
        super.observeStates()
        observeEvent(loginViewModel.userId) {
            navigateToContentFragment()
        }
    }

    private fun navigateToContentFragment() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToContentFragment()
        )
    }
}