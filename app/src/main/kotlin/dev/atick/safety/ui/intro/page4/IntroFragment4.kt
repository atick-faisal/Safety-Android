package dev.atick.safety.ui.intro.page4

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment
import dev.atick.core.ui.theme.JetpackTheme
import dev.atick.safety.ui.intro.page3.IntroScreen3

@AndroidEntryPoint
class IntroFragment4 : BaseFragment() {

    @Composable
    override fun ComposeUi() {
        JetpackTheme {
            IntroScreen4(onNextClick = { /*TODO*/ })
        }
    }

}