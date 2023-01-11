package com.kevin.blogappkotlin.domain.auth

import com.google.firebase.auth.FirebaseUser
import com.kevin.blogappkotlin.data.remote.auth.LoginDataSource

class LoginRepoImplements(private val datasource: LoginDataSource): LoginRepo {
    override suspend fun sigIn(email: String, password: String): FirebaseUser? {
        return datasource.signIn(email,password)
    }
}