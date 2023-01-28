package com.kevin.blogappkotlin.data.remote.Camera

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kevin.blogappkotlin.data.model.Posts
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import kotlin.random.Random

class CameraDataSource   {

    suspend fun uploadPhoto(imagebitmap: Bitmap, descriotion: String){
        val user = FirebaseAuth.getInstance().currentUser
        var randomName = UUID.randomUUID().toString()
        val imageref= FirebaseStorage.getInstance().reference.child("${user?.uid}/posts/$randomName")
        val baos = ByteArrayOutputStream()
        imagebitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val downloadURL = imageref.putBytes(baos.toByteArray()).await().storage.downloadUrl.await().toString()
        val posTime = System.currentTimeMillis()


        user?.let {
            FirebaseFirestore.getInstance().collection("post").add(
                it.displayName?.let { it1 ->
                    Posts(
                        profile_name = it1,
                        profile_pictura = it.photoUrl.toString(),

                        post_image = downloadURL,
                        post_description = descriotion,
                        uid = it.uid
                    )
                }!!
            )
        }


    }
}