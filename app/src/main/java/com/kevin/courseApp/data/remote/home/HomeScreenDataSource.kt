package com.kevin.courseApp.data.remote.home

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.data.model.Posts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class   HomeScreenDataSource {


    /**
     *  implementacion de Flow para datos en tiempo real
     *
     * */
    suspend fun getLatestPost(): Result<List<Posts>>   {
//buscamos la informacion en firebase desde aqui

        //desde aqui agregamos los post a una lista mutable
        val postList = mutableListOf<Posts>()

        withContext(Dispatchers.IO) {
            val querySnapshot = FirebaseFirestore.getInstance().collection("post")
                .orderBy("created_at", Query.Direction.DESCENDING).get().await()

            for (post in querySnapshot.documents) {
                post.toObject(Posts::class.java)?.let { fbPost ->

                    val isLiked = FirebaseAuth.getInstance().currentUser?.let { safeUser ->
                        isPostLiked(post.id, safeUser.uid)
                    }

                    fbPost.apply {
                        created_at = post.getTimestamp(
                            "created_at",
                            DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                        )?.toDate()
                        id = post.id

                        if(isLiked != null) {
                            liked = isLiked
                        }
                    }

                    postList.add(fbPost)
                }
            }
        }
        return Result.Success(postList)
    }

    private suspend fun isPostLiked(postId: String, uid: String): Boolean {
        val post = FirebaseFirestore.getInstance().collection("postsLikes").document(postId).get().await()
        if(!post.exists()) return false
        val likeArray: List<String> = post.get("likes") as List<String>
        return likeArray.contains(uid)
    }

    fun registerLikeButtonState(postId: String, liked: Boolean) {

        val increment = FieldValue.increment(1)
        val decrement = FieldValue.increment(-1)

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val postRef = FirebaseFirestore.getInstance().collection("post").document(postId)
        val postsLikesRef = FirebaseFirestore.getInstance().collection("postsLikes").document(postId)

        val database = FirebaseFirestore.getInstance()

        database.runTransaction { transaction ->
            val snapshot = transaction.get(postRef)
            val likeCount = snapshot.getLong("likes")
            if (likeCount != null) {
                if (likeCount >= 0) {
                    if (liked) {
                        if (transaction.get(postsLikesRef).exists()) {
                            transaction.update(postsLikesRef, "likes", FieldValue.arrayUnion(uid))
                        } else {
                            transaction.set(
                                postsLikesRef,
                                hashMapOf("likes" to arrayListOf(uid)),
                                SetOptions.merge()
                            )
                        }

                        transaction.update(postRef, "likes", increment)
                    } else {
                        transaction.update(postRef, "likes", decrement)
                        transaction.update(postsLikesRef, "likes", FieldValue.arrayRemove(uid))
                    }
                }
            }
        }.addOnFailureListener {
            throw Exception(it.message)
        }
    }
}