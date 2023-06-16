package com.kevin.courseApp.ui.openia.assistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import com.google.android.material.elevation.SurfaceColors
import com.kevin.courseApp.R
import com.kevin.courseApp.ui.fragments.AssistantFragment

class AssistantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val assistantFragment = AssistantFragment()
        assistantFragment.show(supportFragmentManager, "AssistantFragment")

        window.navigationBarColor = SurfaceColors.SURFACE_1.getColor(this)

        assistantFragment.dialog?.setOnDismissListener {
            finish()
        }
    }
}