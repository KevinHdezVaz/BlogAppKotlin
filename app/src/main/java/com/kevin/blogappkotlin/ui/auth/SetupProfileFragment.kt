package com.kevin.blogappkotlin.ui.auth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kevin.blogappkotlin.R
import com.kevin.blogappkotlin.data.remote.auth.AuthDataSource
import com.kevin.blogappkotlin.databinding.FragmentProfileBinding
import com.kevin.blogappkotlin.databinding.FragmentSetupProfileBinding
import com.kevin.blogappkotlin.domain.auth.AuthRepoImplements
import com.kevin.blogappkotlin.presentation.auth.AuthViewModel
import com.kevin.blogappkotlin.presentation.auth.LoginScreeViewModelFactory

class SetupProfileFragment : Fragment(R.layout.fragment_setup_profile) {
    private val REQUEST_IMAGE_CAPTURE = 2

    private val CAMERA_REQUEST_CODE = 0
    private var bitmap: Bitmap? = null
    private lateinit var binding: FragmentSetupProfileBinding
    private val viewmodel by viewModels<AuthViewModel> {
        LoginScreeViewModelFactory(AuthRepoImplements(AuthDataSource()))
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSetupProfileBinding.bind(view)
        binding.profileImage.setOnClickListener{
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "No se encontro app para abir la camara", Toast.LENGTH_SHORT).show()
            }

        }
        requestCameraPermission()

        binding.btnCreateProfile.setOnClickListener{
            val username = binding.username.text.toString().trim()
            val alertDialog = AlertDialog.Builder(
                requireContext()).setTitle("Subiendo foto...").create()

            bitmap.let {
                if(username.isNotEmpty())
                viewmodel.updateUserProfile(imagebitmap = bitmap!!,username).observe(viewLifecycleOwner) {
                    when (it) {
                        is com.kevin.blogappkotlin.core.Result.Loading-> {
                            alertDialog.show()
                        }
                        is com.kevin.blogappkotlin.core.Result.Success -> {
                            alertDialog.dismiss()
                            findNavController().navigate(R.id.action_setupProfileFragment_to_homeFragment)
                        }
                        is com.kevin.blogappkotlin.core.Result.Failure -> {
                            alertDialog.dismiss()
                        }

                    }
                }
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.profileImage.setImageBitmap(imageBitmap)
            bitmap = imageBitmap
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