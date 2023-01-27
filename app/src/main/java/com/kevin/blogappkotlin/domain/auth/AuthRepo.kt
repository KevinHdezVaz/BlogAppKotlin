package com.kevin.blogappkotlin.domain.auth

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import com.google.firebase.auth.FirebaseUser

interface AuthRepo {
    suspend fun sigIn(email: String, password: String): FirebaseUser?
     suspend fun sigUp(email: String, password: String, username: String): FirebaseUser?
    suspend fun updateProfile(imageBitmap: Bitmap, username:String)

}