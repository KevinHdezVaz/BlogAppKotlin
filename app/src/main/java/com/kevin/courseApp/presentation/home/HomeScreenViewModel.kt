package com.kevin.courseApp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.domain.home.HomeScreenRepo

import kotlinx.coroutines.Dispatchers
import java.lang.Exception

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
        emit(Result.Loading())
        kotlin.runCatching {

        repo.getLatestPost()
        }.onSuccess {


                emit(it)

        }.onFailure {
            emit( Result.Failure(Exception(it.message)))
        }
    }


    fun registerLikeButtonState(postId: String,  liked:Boolean) = liveData(viewModelScope.coroutineContext +  Dispatchers.IO) {
        emit(Result.Loading())
        kotlin.runCatching {

            repo.registerLikeButtonState(postId,liked)
        }.onSuccess {

            emit(Result.Success(Unit))

        }.onFailure {
            emit( Result.Failure(Exception(it.message)))
        }
    }

}


//aqui puede dar errores por la version de Factory


class HomeScreenViewModelFactory(private val repo: HomeScreenRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HomeScreenRepo::class.java).newInstance(repo)
    }
}
