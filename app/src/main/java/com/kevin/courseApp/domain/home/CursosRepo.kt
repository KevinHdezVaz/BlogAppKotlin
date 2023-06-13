package com.kevin.courseApp.domain.home



import com.kevin.courseApp.core.Result
import com.kevin.courseApp.data.model.Cursos

interface CursosRepo
{

    suspend fun getCursosAllRepo():  Result<List<Cursos>>

    suspend fun getFavoritos():  Result<List<Cursos>>
    suspend fun getCoursesNew():  Result<List<Cursos>>
    suspend fun getCourseAll():  Result<List<Cursos>>

    suspend fun getCursoFiltrado(category: String):  Result<List<Cursos>>
}