package com.kevin.courseApp.ui.openia.permission

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kevin.courseApp.R

class MicrophonePermisoActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                this.setResult(RESULT_OK)
                finish()
            } else {
                MaterialAlertDialogBuilder(this , R.style.AlertDialogTheme)
                    .setTitle("SpeakGPT")
                    .setMessage("You can not use this feature because app do not have microphone access.")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int ->
                        this.setResult(RESULT_CANCELED)
                        finish()
                    }.show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun askNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            setResult(RESULT_OK)
            finish()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
            MaterialAlertDialogBuilder(this )
                .setTitle("SpeakGPT")
                .setMessage("SpeakGPT allows you to interact with ChatGPT via voice activation. To enable voice activation please allow this app to use your device microphone.")
                .setCancelable(false)
                .setPositiveButton("Allow") { _: DialogInterface?, _: Int ->
                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }.setNegativeButton("No thanks") { _: DialogInterface?, _: Int ->
                    this.setResult(RESULT_CANCELED)
                    finish()
                }.show()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }
}