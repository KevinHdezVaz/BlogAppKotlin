package com.kevin.courseApp.domain.auth

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseUser

interface AuthRepo {
    suspend fun sigIn(email: String, password: String): FirebaseUser?
     suspend fun sigUp(email: String, password: String, username: String): FirebaseUser?
    suspend fun updateProfile(imageBitmap: Bitmap, username:String)

}