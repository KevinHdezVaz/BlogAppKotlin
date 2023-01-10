package com.kevin.blogappkotlin.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.kevin.blogappkotlin.core.Resource
import com.kevin.blogappkotlin.data.model.Posts
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {

    suspend fun getLatestPost(): Resource<List<Posts>>{
//buscamos la informacion en firebase desde aqui

        //desde aqui agregamos los post a una lista mutable
        val postList = mutableListOf<Posts>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("post").get().await()

        for(post in querySnapshot.documents){
            post.toObject(Posts::class.java)?.let {
                postList.add(it)
            }
        }

        return Resource.Success(postList)
    }
}