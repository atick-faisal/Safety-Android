package dev.atick.safety.ui.intro.page1

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment
import dev.atick.core.ui.theme.JetpackTheme

@AndroidEntryPoint
class IntroFragment1 : BaseFragment() {

    @Composable
    override fun ComposeUi() {
        JetpackTheme {
            IntroScreen1(onNextClick = ::navigateToIntroPage2)
        }
    }

    private fun navigateToIntroPage2() {
        findNavController().navigate(
            IntroFragment1Directions.actionIntroFragment1ToIntroFragment2()
        )
    }

}