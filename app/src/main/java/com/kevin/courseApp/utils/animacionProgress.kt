package com.kevin.courseApp.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.airbnb.lottie.LottieAnimationView
import com.kevin.courseApp.R

class animacionProgress {

    companion object {
        private const val TAG = "HomeFragment"
        private var animacion: LottieAnimationView? = null
        private var epicDialog2: Dialog? = null

        @SuppressLint("SuspiciousIndentation")
        fun mostrarCarga(context: Context) {
            epicDialog2 = Dialog(context)
            epicDialog2!!.setContentView(R.layout.progress_layout)

            epicDialog2!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            epicDialog2!!.setCanceledOnTouchOutside(false)
            epicDialog2!!.show()
        }

        fun esconderCarga( ) {

            epicDialog2!!.dismiss()
        }
    }

}