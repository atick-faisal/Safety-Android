package dev.atick.safety.ui.content.notifications

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment

@AndroidEntryPoint
class NotificationFragment : BaseFragment() {

    @Composable
    override fun ComposeUi() {
        NotificationScreen()
    }

}