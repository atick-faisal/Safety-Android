package dev.atick.safety.ui.intro.start

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment
import dev.atick.core.ui.theme.JetpackTheme

@AndroidEntryPoint
class StartFragment : BaseFragment() {

    @Composable
    override fun ComposeUi() {
        JetpackTheme {
            StartScreen(onNextClick = { /*TODO*/ })
        }
    }

}