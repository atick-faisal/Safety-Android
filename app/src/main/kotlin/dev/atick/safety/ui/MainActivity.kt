package dev.atick.safety.ui

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.bluetooth.utils.BtUtils
import dev.atick.core.ui.extensions.checkForPermissions
import dev.atick.core.ui.extensions.resultLauncher
import dev.atick.safety.R
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val permissions = mutableListOf<String>()
    private lateinit var btLauncher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var btUtils: BtUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_JetpackComposeStarter)
        setContentView(R.layout.activity_main)

        //                ... App Permissions ...
        // ----------------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }
        permissions.add(Manifest.permission.READ_SMS)
        permissions.add(Manifest.permission.SEND_SMS)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        checkForPermissions(permissions)

        //              ... Turn On Bluetooth ...
        // ------------------------------------------------------
        btLauncher = resultLauncher(onFailure = { finishAffinity() })
        if (!btUtils.isEnabled) {
            btLauncher.launch(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            )
        }
    }
}