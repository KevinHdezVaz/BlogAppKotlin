package com.kevin.courseApp.domain.auth

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseUser
import com.kevin.courseApp.data.remote.auth.AuthDataSource

class AuthRepoImplements(private val datasource: AuthDataSource): AuthRepo {

    override suspend fun sigIn(email: String, password: String): FirebaseUser? {
        return datasource.signIn(email,password)
    }

    override suspend fun sigUp(email: String, password: String, username: String): FirebaseUser? {
         return datasource.signUp(email,password,username)
    }

    override suspend fun updateProfile(imageBitmap: Bitmap, username: String) {
         return datasource.updateUserProfile(imageBitmap,username)
    }
}