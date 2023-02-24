package com.kevin.courseApp.data.model

import java.io.Serializable


data class Cursos(
    val titulo: String = "",
    val descripcion: String = "",
    val imagenUrl: String = "",
    val enlace: String = "",
    val categoria: String = "",
    val empresa: String = "",
    val imagenFondo: String="",
    val certificado: Boolean = false // <-- Agregar esta propiedad
)
    : Serializable