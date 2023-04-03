package com.kevin.courseApp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.domain.home.CursosRepo

import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class HomeScreenViewModel(private val repo: CursosRepo): ViewModel() {
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

    fun getCursosFiltrados(categoria : String) = liveData (Dispatchers.IO){
        emit(Result.Loading())
        kotlin.runCatching {
            repo.getCursoFiltrado(categoria)
        }.onSuccess {
            emit(it)

        }.onFailure {
            emit( Result.Failure(Exception(it.message)))
        }
    }
    fun getFavoritos() = liveData (Dispatchers.IO){
        emit(Result.Loading())
        kotlin.runCatching {
            repo.getFavoritos()
        }.onSuccess {
            emit(it)

        }.onFailure {
            emit( Result.Failure(Exception(it.message)))
        }
    }
}

class HomeScreenViewModelFactory(private val repo: CursosRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CursosRepo::class.java).newInstance(repo)
    }
}
