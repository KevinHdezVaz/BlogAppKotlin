package com.kevin.courseApp.domain.camera

import android.graphics.Bitmap

interface  CameraRepositorio {

    suspend fun uploadPhot(imageBitmap: Bitmap, description: String)
}