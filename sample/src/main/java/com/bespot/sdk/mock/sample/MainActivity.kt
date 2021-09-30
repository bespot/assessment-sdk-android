package com.bespot.sdk.mock.sample

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.BLUETOOTH
import android.Manifest.permission.BLUETOOTH_ADMIN
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bespot.sdk.BuildConfig
import com.bespot.sdk.mock.sample.databinding.ActivityMainBinding
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.coroutines.sendSuspend
import com.fondesa.kpermissions.extension.permissionsBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup subscribe/unsubscribe button
        binding.startSession.setOnClickListener { checkPermission(Action.START_SESSION) }
        binding.pickStore.setOnClickListener { checkPermission(Action.PICK_STORE) }
        binding.startScanning.setOnClickListener { checkPermission(Action.SCAN) }
        binding.version.text = BuildConfig.VERSION_NAME
    }

    private fun checkPermission(action: Action) {
        CoroutineScope(Dispatchers.Main).launch {
            val hasPermission =
                permissionsBuilder(ACCESS_FINE_LOCATION, BLUETOOTH, BLUETOOTH_ADMIN)
                    .build()
                    .sendSuspend()

            if (hasPermission.allGranted()) {
                when (action) {
                    Action.PICK_STORE -> {
                        Timber.d("Pick store button pressed")
                        Toast.makeText(this@MainActivity, "Not available", Toast.LENGTH_SHORT)
                            .show()
                    }
                    Action.SCAN -> {
                        Timber.d("Scan button pressed")
                        Toast.makeText(this@MainActivity, "Not available", Toast.LENGTH_SHORT)
                            .show()
                    }
                    Action.START_SESSION -> {
                        Timber.d("Start session button pressed")
                        SessionActivity.start(this@MainActivity)
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Permissions denied!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    enum class Action {
        PICK_STORE, SCAN, START_SESSION
    }
}
