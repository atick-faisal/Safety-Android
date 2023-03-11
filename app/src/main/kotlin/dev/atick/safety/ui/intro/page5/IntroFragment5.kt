package dev.atick.safety.ui.intro.page5

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment
import dev.atick.core.ui.theme.JetpackTheme

@AndroidEntryPoint
class IntroFragment5 : BaseFragment() {

    @Composable
    override fun ComposeUi() {
        JetpackTheme {
            IntroScreen5(
                onNextClick = ::navigateToLoginScreen,
                onBackClick = ::navigateToIntroPage4
            )
        }
    }

    private fun navigateToLoginScreen() {
        findNavController().navigate(
            IntroFragment5Directions.actionIntroFragment5ToLoginFragment()
        )
    }

    private fun navigateToIntroPage4() {
        findNavController().popBackStack()
    }

}