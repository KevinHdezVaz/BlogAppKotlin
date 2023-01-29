package com.kevin.blogappkotlin.data.remote.auth

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kevin.blogappkotlin.data.model.User
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class AuthDataSource {
    suspend fun signIn(email:String, password:String):FirebaseUser?{
//obtenemos datos de firebase
    val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).await()
    return authResult.user
    }

   suspend fun signUp(email: String, password: String, username: String): FirebaseUser? {
       val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).await()
       authResult.user?.uid?.let {
           FirebaseFirestore.getInstance().collection("users").document(it).set(User(
               email,username,
           )).await()

       }
        return authResult.user
    }

    suspend fun updateUserProfile(imagebitmap: Bitmap, username:String){
        val user = FirebaseAuth.getInstance().currentUser
        val imageref= FirebaseStorage.getInstance().reference.child("${user?.uid}/profile_picture")
        val baos = ByteArrayOutputStream()
        imagebitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val downloadURL = imageref.putBytes(baos.toByteArray()).await().storage.downloadUrl.await().toString()

        val profileUpdates = userProfileChangeRequest {
            displayName = username
            photoUri = Uri.parse(downloadURL)
            build()

        }
        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }

    }
}