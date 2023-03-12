package dev.atick.safety.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.core.ui.extensions.checkForPermissions
import dev.atick.safety.R
import dev.atick.sms.data.SmsDataSource
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var smsDataSource: SmsDataSource

    private val permissions = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_JetpackComposeStarter)
        setContentView(R.layout.activity_main)

        //                ... App Permissions ...
        // ----------------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        permissions.add(Manifest.permission.READ_SMS)
        permissions.add(Manifest.permission.SEND_SMS)
        checkForPermissions(permissions)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val emergencyMessages =
                    smsDataSource.getEmergencyMessages(listOf("+97466901695", "Vodafone"))
                Logger.d("MESSAGES: $emergencyMessages")
            }
        }
    }
}