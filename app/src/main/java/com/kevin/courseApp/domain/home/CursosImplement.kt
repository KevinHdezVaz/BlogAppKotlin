package com.kevin.courseApp.domain.home

import com.kevin.courseApp.core.Result
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.data.remote.home.CursosDataSource


class CursosImplement(val dataSource: CursosDataSource ): CursosRepo {


     override suspend fun getCursosAllRepo(): Result<List<Cursos>> = dataSource.getCursosDataSource()
     //agregar esto para mvvm y varias peticiones, para que cada metodo tenga diferenet parametro
     
     override suspend fun getCursoFiltrado(): Result<List<Cursos>> = dataSource.cursosFiltradoCategory("leer")


}