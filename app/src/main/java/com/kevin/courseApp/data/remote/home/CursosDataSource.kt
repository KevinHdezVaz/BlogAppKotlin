package com.kevin.courseApp.data.remote.home

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.data.model.Cursos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CursosDataSource {

    suspend fun getCursosDataSource(): Result<List<Cursos>> {
        val postList = mutableListOf<Cursos>()
        try {
            val dataSnapshot = FirebaseDatabase.getInstance().reference.child("cursos").get().await()
            for (postSnapshot in dataSnapshot.children) {
                val curso = postSnapshot.getValue(Cursos::class.java)

                curso?.let { postList.add(it) }
               postList.sortBy { it.titulo }
            }
            return Result.Success(postList)
        } catch (e: Exception) {
            return Result.Failure(e)
        }
    }





}
