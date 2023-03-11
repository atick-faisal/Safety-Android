package dev.atick.safety.ui.intro.page4

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment
import dev.atick.core.ui.theme.JetpackTheme

@AndroidEntryPoint
class IntroFragment4 : BaseFragment() {

    @Composable
    override fun ComposeUi() {
        JetpackTheme {
            IntroScreen4(
                onNextClick = ::navigateToIntroPage5,
                onBackClick = ::navigateToIntroPage3
            )
        }
    }

    private fun navigateToIntroPage5() {
        findNavController().navigate(
            IntroFragment4Directions.actionIntroFragment4ToIntroFragment5()
        )
    }

    private fun navigateToIntroPage3() {
        findNavController().popBackStack()
    }
}