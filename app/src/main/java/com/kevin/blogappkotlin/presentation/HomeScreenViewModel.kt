package com.kevin.blogappkotlin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.kevin.blogappkotlin.core.Resource
import com.kevin.blogappkotlin.domain.HomeScreenRepo

import kotlinx.coroutines.Dispatchers

class HomeScreenViewModel(private val repo: HomeScreenRepo): ViewModel() {
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

    fun fetchLatestPosts() = liveData (Dispatchers.IO){
        emit(Resource.Loading())
        try{
            emit(repo.getLatestPost())
        }catch (e:Exception){

            emit(Resource.Failure(e))
        }
    }
}


//aqui puede dar errores por la version de Factory


class HomeScreenViewModelFactory(private val repo: HomeScreenRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HomeScreenRepo::class.java).newInstance(repo)
    }
}
