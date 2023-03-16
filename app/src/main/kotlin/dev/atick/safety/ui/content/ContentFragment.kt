package dev.atick.safety.ui.content

import android.content.Intent
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment
import dev.atick.safety.data.devices.SafetyDevice
import dev.atick.safety.service.SafetyService

@AndroidEntryPoint
class ContentFragment : BaseFragment() {

    @Composable
    override fun ComposeUi() {
        ContentScreen(::startSafetyService, ::stopSafetyService)
    }

    private fun startSafetyService(device: SafetyDevice) {
        val intent = Intent(requireContext(), SafetyService::class.java)
        intent.putExtra(SafetyService.DEVICE_ADDRESS_KEY, device.address)
        requireContext().startService(intent)
    }

    private fun stopSafetyService() {
        val intent = Intent(requireContext(), SafetyService::class.java)
        intent.action = SafetyService.ACTION_STOP_SERVICE
        requireContext().startService(intent)
    }

}