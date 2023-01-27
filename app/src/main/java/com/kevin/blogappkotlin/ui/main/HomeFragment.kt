package com.kevin.blogappkotlin.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kevin.blogappkotlin.R
import com.kevin.blogappkotlin.core.Result
import com.kevin.blogappkotlin.core.hide
import com.kevin.blogappkotlin.core.show
import com.kevin.blogappkotlin.data.remote.home.HomeScreenDataSource
import com.kevin.blogappkotlin.databinding.FragmentHomeBinding
import com.kevin.blogappkotlin.domain.home.HomeScreenRepoImplement
import com.kevin.blogappkotlin.presentation.HomeScreenViewModel
import com.kevin.blogappkotlin.presentation.HomeScreenViewModelFactory
import com.kevin.blogappkotlin.ui.main.adapter.HomeScreenAdapter

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    private val CAMERA_REQUEST_CODE = 0
    private val viewmodel by viewModels<HomeScreenViewModel>
    {
    (HomeScreenViewModelFactory(
            HomeScreenRepoImplement(HomeScreenDataSource())
        ))
    }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            super.onViewCreated(view, savedInstanceState)
            binding = FragmentHomeBinding.bind(view)

            //permisos camara
            checkCameraPermission()
            requestCameraPermission()
            viewmodel.fetchLatestPosts().observe(viewLifecycleOwner, Observer {

                when(it){
                    is  Result.Loading ->{

                        binding.progresBar.show()
                     }
                    is  Result.Success ->{
                        binding.progresBar.hide()

                        if(it.data.isEmpty()){
                            binding.emptyContainer.show()
                            return@Observer
                        }else{
                            binding.emptyContainer.hide()
                        }
                        binding.rvHome.adapter  = HomeScreenAdapter(it.data)

                    }
                    is  Result.Failure ->{
                        binding.progresBar.visibility = View.GONE
                        Toast.makeText(requireContext(),"Ocurrio un error ${it.exception}",Toast.LENGTH_SHORT).show()

                    }
                }
            })
            //    binding.rvHome.adapter = HomeScreenAdapter(postlist = postlist)

    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            //El permiso no está aceptado.
            requestCameraPermission()
        } else {
            //El permiso está aceptado.
        }
    }
    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.CAMERA)) {
            //El usuario ya ha rechazado el permiso anteriormente, debemos informarle que vaya a ajustes.
        } else {
            //El usuario nunca ha aceptado ni rechazado, así que le pedimos que acepte el permiso.
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE)
        }
    } 


}
