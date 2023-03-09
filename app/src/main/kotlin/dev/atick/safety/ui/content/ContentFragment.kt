package dev.atick.safety.ui.content

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment

@AndroidEntryPoint
class ContentFragment : BaseFragment() {

    @Composable
    override fun ComposeUi() {
        ContentScreen()
    }

}