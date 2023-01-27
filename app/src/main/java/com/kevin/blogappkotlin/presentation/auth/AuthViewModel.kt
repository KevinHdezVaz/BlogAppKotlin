package com.kevin.blogappkotlin.presentation.auth

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.kevin.blogappkotlin.core.Result
import com.kevin.blogappkotlin.domain.auth.AuthRepo
import kotlinx.coroutines.Dispatchers

class AuthViewModel(private val repo: AuthRepo):ViewModel() {

    fun SignIn(email:String, password:String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())

        try {
        emit ( Result.Success(repo.sigIn(email,password)))

        }catch (e:Exception){
    emit(Result.Failure(e))
        }
    }

    fun SignUp(email:String, password:String,username:String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())

        try {
            emit ( Result.Success(repo.sigUp(email,password,username)))

        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }


    fun updateUserProfile(imagebitmap:Bitmap, username:String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())

        try {
            emit ( Result.Success(repo.updateProfile(imagebitmap, username )))

        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }

}



class LoginScreeViewModelFactory(private val repo: AuthRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repo) as T
    }
}