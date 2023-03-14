package com.kevin.courseApp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kevin.courseApp.data.model.Cursos
@Database(entities = [Cursos::class], version = 1)
abstract class AppDataBaseCursoGuardado : RoomDatabase() {
    abstract fun cursoDao(): CursoDao
}
