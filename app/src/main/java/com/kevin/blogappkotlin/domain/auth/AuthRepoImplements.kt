package com.kevin.blogappkotlin.domain.auth

import com.google.firebase.auth.FirebaseUser
import com.kevin.blogappkotlin.data.remote.auth.AuthDataSource

class AuthRepoImplements(private val datasource: AuthDataSource): AuthRepo {

    override suspend fun sigIn(email: String, password: String): FirebaseUser? {
        return datasource.signIn(email,password)
    }

    override suspend fun sigUp(email: String, password: String, username: String): FirebaseUser? {
         return datasource.signUp(email,password,username)
    }
}