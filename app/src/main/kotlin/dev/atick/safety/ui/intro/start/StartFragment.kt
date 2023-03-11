package dev.atick.safety.ui.intro.start

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment
import dev.atick.core.ui.theme.JetpackTheme

@AndroidEntryPoint
class StartFragment : BaseFragment() {

    private val startViewModel: StartViewModel by viewModels()

    @Composable
    override fun ComposeUi() {
        JetpackTheme {
            StartScreen()
        }
    }

    override fun observeStates() {
        super.observeStates()
        startViewModel.userId.observe(this) {
            it?.getContentIfNotHandled()?.let {
                navigateToContentScreen()
            } ?: navigateToIntroScreen1()

        }
    }

    private fun navigateToIntroScreen1() {
        findNavController().navigate(
            StartFragmentDirections.actionStartFragmentToIntroFragment1()
        )
    }

    private fun navigateToContentScreen() {
        findNavController().navigate(
            StartFragmentDirections.actionStartFragmentToContentFragment()
        )
    }
}