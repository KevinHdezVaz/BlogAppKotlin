package com.kevin.blogappkotlin.data.remote.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class LoginDataSource {
    suspend fun signIn(email:String, password:String):FirebaseUser?{
//obtenemos datos de firebase

        val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).await()
    return authResult.user
    }
}