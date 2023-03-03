package dev.atick.safety.ui.intro.screen1

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment
import dev.atick.core.ui.theme.JetpackTheme

@AndroidEntryPoint
class IntroFragment1 : BaseFragment() {

    @Composable
    override fun ComposeUi() {
        JetpackTheme {
            IntroScreen1(onNextClick = { /*TODO*/ })
        }
    }

}