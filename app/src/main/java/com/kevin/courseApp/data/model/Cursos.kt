package com.kevin.courseApp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


data class Cursos(

    val titulo: String = "",
    val descripcion: String = "",
    val imagenUrl: String = "",
    val enlace: String = "",
    val categoria: String = "",
    val empresa: String = "",
    val imagenFondo: String="",
    //detalles curso
    val valoracion :Double=0.0,
    val duracion:Int=0,
    val idioma:String="",
    val estudiantes:Double=0.0,
    val certificado: Boolean = false
)
    :Serializable

 
