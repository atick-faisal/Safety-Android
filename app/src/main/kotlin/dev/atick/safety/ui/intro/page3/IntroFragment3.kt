package dev.atick.safety.ui.intro.page3

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment
import dev.atick.core.ui.theme.JetpackTheme

@AndroidEntryPoint
class IntroFragment3 : BaseFragment() {

    @Composable
    override fun ComposeUi() {
        JetpackTheme {
            IntroScreen3(
                onNextClick = ::navigateToIntroPage4,
                onBackClick = ::navigateToIntroPage2
            )
        }
    }

    private fun navigateToIntroPage4() {
        findNavController().navigate(
            IntroFragment3Directions.actionIntroFragment3ToIntroFragment4()
        )
    }

    private fun navigateToIntroPage2() {
        findNavController().popBackStack()
    }
}