package com.kevin.blogappkotlin.data.remote.home

import com.google.firebase.firestore.FirebaseFirestore
import com.kevin.blogappkotlin.core.Result
import com.kevin.blogappkotlin.data.model.Posts
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {

    suspend fun getLatestPost(): Result<List<Posts>> {
//buscamos la informacion en firebase desde aqui

        //desde aqui agregamos los post a una lista mutable
        val postList = mutableListOf<Posts>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("post").get().await()

        for(post in querySnapshot.documents){
            post.toObject(Posts::class.java)?.let {
                postList.add(it)
            }
        }

        return Result.Success(postList)
    }
}