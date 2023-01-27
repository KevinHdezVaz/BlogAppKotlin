package com.kevin.blogappkotlin.domain.camera

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap

interface  CameraRepositorio {

    suspend fun uploadPhot(imageBitmap: Bitmap, description: String)
}