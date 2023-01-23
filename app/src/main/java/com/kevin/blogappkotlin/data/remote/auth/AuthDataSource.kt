package com.kevin.blogappkotlin.data.remote.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.kevin.blogappkotlin.data.model.User
import kotlinx.coroutines.tasks.await

class AuthDataSource {
    suspend fun signIn(email:String, password:String):FirebaseUser?{
//obtenemos datos de firebase
    val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).await()
    return authResult.user
    }

   suspend fun signUp(email: String, password: String, username: String): FirebaseUser? {
       val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).await()
       authResult.user?.uid?.let {
           FirebaseFirestore.getInstance().collection("users").document(it).set(User(email,username,"FOTO_URL:PNG")).await()

       }
        return authResult.user
    }
}