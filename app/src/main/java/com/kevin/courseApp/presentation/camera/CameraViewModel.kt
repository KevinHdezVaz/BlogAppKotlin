package com.kevin.courseApp.presentation.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.domain.camera.CameraRepositorio
import kotlinx.coroutines.Dispatchers

class CameraViewModel(private val repo: CameraRepositorio): ViewModel()
{

    fun uploadPhoto(imageBitmap: Bitmap, descriptio: String, ) = liveData(Dispatchers.IO){

        emit(    Result.Loading())
        try{
            emit ( Result.Success(repo.uploadPhot(imageBitmap,descriptio)))

        }catch (e:Exception){
            emit(Result.Failure(e))
        }

    }
    class CameraViewmodelFactory(private val repo: CameraRepositorio) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CameraViewModel(repo) as T
        }
    }
}
