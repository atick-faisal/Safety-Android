package dev.atick.safety.ui.auth.login

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment
import dev.atick.core.ui.theme.JetpackTheme

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    @Composable
    override fun ComposeUi() {
        JetpackTheme {
            LoginScreen(::navigateToContentFragment)
        }
    }

    private fun navigateToContentFragment() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToContentFragment()
        )
    }
}