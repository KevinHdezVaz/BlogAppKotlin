package com.kevin.blogappkotlin.presentation.auth

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.kevin.blogappkotlin.core.Resource
import com.kevin.blogappkotlin.domain.auth.LoginRepo
import com.kevin.blogappkotlin.domain.home.HomeScreenRepo
import kotlinx.coroutines.Dispatchers

class LoginScreeViewModel(private val repo: LoginRepo):ViewModel() {

    fun SignIn(email:String, password:String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())

        try {
        emit ( Resource.Success(repo.sigIn(email,password)))

        }catch (e:Exception){
    emit(Resource.Failure(e))
        }
    }

}



class LoginScreeViewModelFactory(private val repo: LoginRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginScreeViewModel(repo) as T
    }
}