package com.kevin.courseApp.data.model



data class Cursos(
    val titulo: String = "",
    val descripcion: String = "",
    val imagenUrl: String = "",
    val enlace: String = ""
) {
    constructor() : this("", "", "", "")
}
