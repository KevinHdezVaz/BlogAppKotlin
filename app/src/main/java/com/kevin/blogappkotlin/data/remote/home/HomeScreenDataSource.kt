package com.kevin.blogappkotlin.data.remote.home

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kevin.blogappkotlin.core.Result
import com.kevin.blogappkotlin.data.model.Posts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class   HomeScreenDataSource {


    /**
     *  implementacion de Flow para datos en tiempo real
     *
     * */
    suspend fun getLatestPost(): Flow<Result<List<Posts>>> = callbackFlow   {
//buscamos la informacion en firebase desde aqui

        //desde aqui agregamos los post a una lista mutable
        val postList = mutableListOf<Posts>()
        var postReference  : Query?= null
        try {
            postReference = FirebaseFirestore.getInstance().collection("post").orderBy("created_at", Query.Direction.DESCENDING)

        }catch (e: Throwable){
       //solo se usa con FLOW
        close(e)
        }

        val suscription = postReference?.addSnapshotListener{ value, error ->
            if (value != null) {
                try{
                    postList.clear()
                    for(post in value.documents){
                        post.toObject(Posts::class.java)?.let {
                            it.apply {
                                created_at = post.getTimestamp("created_at",DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)?.toDate()
                                postList.add(it)
                            }

                        }


                    }
                }catch (e:Exception){
                        close(e)
                }

            }

            trySend(Result.Success(postList)).isSuccess

        }
                awaitClose{suscription?.remove()}
    }
}