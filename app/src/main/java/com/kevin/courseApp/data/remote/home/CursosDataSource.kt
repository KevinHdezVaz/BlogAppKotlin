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


    suspend fun getCursosDataSourceAll(): Result<List<Cursos>> {
        val postList = mutableListOf<Cursos>()
        try {
            val dataSnapshot = FirebaseDatabase.getInstance().reference.child("cursos")
                .get().await()
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

    suspend fun getCursosDataSource( ): Result<List<Cursos>> {
        val postList = mutableListOf<Cursos>()
        try {
            val dataSnapshot = FirebaseDatabase.getInstance().reference.child("cursos")

                .get().await()

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


    suspend fun getLatestCoursesDataSource( ): Result<List<Cursos>> {
        val postList = mutableListOf<Cursos>()
        try {
            val dataSnapshot = FirebaseDatabase.getInstance().reference.
            child("cursos")

                .get().await()
            for (postSnapshot in dataSnapshot.children) {
                val curso = postSnapshot.getValue(Cursos::class.java)
                curso?.let { postList.add(it) }
            }
            // Ordenar los cursos por el campo "estudiantes" de manera descendente
            postList.sortByDescending { it.estudiantes }


            return Result.Success(postList)
        } catch (e: Exception) {
            return Result.Failure(e)
        }
    }



    suspend fun getFavDatasource(): Result<List<Cursos>> {
        val postList = mutableListOf<Cursos>()
        try {
            val mAuth = FirebaseAuth.getInstance()
            val userId = mAuth.currentUser?.uid
             val dataSnapshot = FirebaseDatabase.getInstance().getReference("usuarios/$userId/cursosGuardados").get().await()
            for (postSnapshot in dataSnapshot.children) {
                val curso = postSnapshot.getValue(Cursos::class.java)

                curso?.let { postList.add(it) }


            }

            return Result.Success(postList)
        } catch (e: Exception) {
            return Result.Failure(e)
        }
    }

    suspend fun cursosFiltradoCategory(categoria: String): Result<List<Cursos>> {
        val postList = mutableListOf<Cursos>()
        try {
            val dataSnapshot = FirebaseDatabase.getInstance().reference.child("cursos").get().await()
            for (postSnapshot in dataSnapshot.children) {
                val curso = postSnapshot.getValue(Cursos::class.java)

                // Filtrar cursos por categoría
                if (curso?.categoria == categoria) {
                    curso.let { postList.add(it) }
                }
                //por aqui las ordena por nombre

            }
            return Result.Success(postList)
        } catch (e: Exception) {
            return Result.Failure(e)
        }
    }


}
