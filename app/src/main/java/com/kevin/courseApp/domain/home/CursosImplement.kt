package com.kevin.courseApp.domain.home

import com.kevin.courseApp.core.Result
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.data.remote.home.CursosDataSource


class CursosImplement(val dataSource: CursosDataSource ): CursosRepo {


     override suspend fun getCursosAllRepo(): Result<List<Cursos>> = dataSource.getCursosDataSource()

     override suspend fun getFavoritos():   Result<List<Cursos>> = dataSource.getFavDatasource()
     override suspend fun getCoursesNew(): Result<List<Cursos>> =  dataSource.getLatestCoursesDataSource(11)

     override suspend fun getCursoFiltrado(category: String): Result<List<Cursos>> = dataSource.cursosFiltradoCategory(category)


}