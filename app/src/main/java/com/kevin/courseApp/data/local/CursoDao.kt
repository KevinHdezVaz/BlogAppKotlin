package com.kevin.courseApp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kevin.courseApp.data.model.Cursos

@Dao
interface CursoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cursos: List<Cursos>)

//    @Query("SELECT * FROM cursos")
    fun getAll(): LiveData<List<Cursos>>
}