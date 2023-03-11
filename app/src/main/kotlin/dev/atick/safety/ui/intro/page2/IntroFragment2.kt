package dev.atick.safety.ui.intro.page2

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment
import dev.atick.core.ui.theme.JetpackTheme

@AndroidEntryPoint
class IntroFragment2 : BaseFragment() {

    @Composable
    override fun ComposeUi() {
        JetpackTheme {
            IntroScreen2(
                onNextClick = ::navigateToIntroPage3,
                onBackClick = ::navigateToIntroPage1
            )
        }
    }

    private fun navigateToIntroPage3() {
        findNavController().navigate(
            IntroFragment2Directions.actionIntroFragment2ToIntroFragment3()
        )
    }

    private fun navigateToIntroPage1() {
        findNavController().popBackStack()
    }
}