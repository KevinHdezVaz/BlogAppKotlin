package com.kevin.courseApp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kevin.courseApp.data.model.Cursos

@Database(entities = [Cursos::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cursoDao(): CursoDao
}
