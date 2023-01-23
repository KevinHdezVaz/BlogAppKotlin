package com.kevin.blogappkotlin.domain.auth

import com.google.firebase.auth.FirebaseUser

interface AuthRepo {
    suspend fun sigIn(email: String, password: String): FirebaseUser?
     suspend fun sigUp(email: String, password: String, username: String): FirebaseUser?
}