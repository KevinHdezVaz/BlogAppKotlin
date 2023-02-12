package com.kevin.courseApp.domain.camera

import android.graphics.Bitmap
import com.kevin.courseApp.data.remote.Camera.CameraDataSource

class CameraRepoImplem(private val datasource: CameraDataSource): CameraRepositorio {
    override suspend fun uploadPhot(imageBitmap: Bitmap, description: String) {

        datasource.uploadPhoto(imageBitmap,description)
    }
}