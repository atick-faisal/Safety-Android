package dev.atick.safety.ui.content

import android.content.Intent
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.base.BaseFragment
import dev.atick.safety.R
import dev.atick.safety.data.devices.SafetyDevice
import dev.atick.safety.service.SafetyService

@AndroidEntryPoint
class ContentFragment : BaseFragment() {

    private var mediaPlayer: MediaPlayer? = null

    @Composable
    override fun ComposeUi() {
        ContentScreen(::startSafetyService, ::stopSafetyService, ::playAlarmSound)
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

    private fun playAlarmSound(play: Boolean) {
        if (play) {
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.alarm)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        } else {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

}