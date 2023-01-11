package com.kevin.blogappkotlin.domain.auth

import com.google.firebase.auth.FirebaseUser

interface LoginRepo {
    suspend fun sigIn(email: String, password: String): FirebaseUser?
}