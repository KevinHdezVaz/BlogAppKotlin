package com.kevin.courseApp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.domain.home.CursosRepo

import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class HomeScreenViewModel(private val repo: CursosRepo): ViewModel() {
/*
* OJO: A UN VIEWMODEL NO SE LE PUEDE AGREGAR UN CONSTRUCTOR, NI PASAR DATOS EN PARAMETRO
*
* SE INICIALIZA CON UN CONSTRUCTOR VACIO.
* PERO SE SOLUCIONA CON UNA FACTORY CLASS.
*
* EL FACTORY GENERA UNA INSTANCIA DEL VIEWMODEL CON ESOS PARAMETROS EN EL CONSTRUCTOR
*
* */

    //EN ESTA PARTE YA ESTA TODE LISTO PARA IR A BUSCAR LA INFORMACION AL SERVIDOR

    fun getCursos() = liveData (Dispatchers.IO){
        emit(Result.Loading())
        kotlin.runCatching {
        repo.getCursosAllRepo()
        }.onSuccess {
             emit(it)

        }.onFailure {
            emit( Result.Failure(Exception(it.message)))
        }
    }

    fun getCursosFiltrados() = liveData (Dispatchers.IO){
        emit(Result.Loading())
        kotlin.runCatching {
            repo.getCursoFiltrado()
        }.onSuccess {
            emit(it)

        }.onFailure {
            emit( Result.Failure(Exception(it.message)))
        }
    }
}

//aqui puede dar errores por la version de Factory


class HomeScreenViewModelFactory(private val repo: CursosRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CursosRepo::class.java).newInstance(repo)
    }
}
