package dev.atick.safety.ui.intro.page3

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment
import dev.atick.core.ui.theme.JetpackTheme
import dev.atick.safety.ui.intro.page2.IntroScreen2

@AndroidEntryPoint
class IntroFragment3 : BaseFragment() {

    @Composable
    override fun ComposeUi() {
        JetpackTheme {
            IntroScreen3(onNextClick = { /*TODO*/ })
        }
    }

}