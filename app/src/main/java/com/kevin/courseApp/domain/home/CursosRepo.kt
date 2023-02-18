package com.kevin.courseApp.domain.home

import com.kevin.courseApp.data.model.Cursos

import com.kevin.courseApp.core.Result

interface CursosRepo
{

    suspend fun getCursosAllRepo():  Result<List<Cursos>>
}