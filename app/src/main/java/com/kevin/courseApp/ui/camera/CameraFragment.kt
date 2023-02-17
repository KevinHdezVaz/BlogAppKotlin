package com.kevin.courseApp.ui.camera


import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kevin.courseApp.R
import com.kevin.courseApp.data.remote.Camera.CameraDataSource
import com.kevin.courseApp.databinding.FragmentCameraBinding
import com.kevin.courseApp.domain.camera.CameraRepoImplem
import com.kevin.courseApp.presentation.camera.CameraViewModel
import java.io.ByteArrayOutputStream
import java.util.*


class CameraFragment : Fragment(R.layout.fragment_camera) {
    private lateinit var binding: FragmentCameraBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent?>
    private val REQUEST_IMAGE_CAPTURE = 2
    private var bitmap: Bitmap? = null
    private val CAMERA_REQUEST_CODE = 0

    private val viewmodel by viewModels<CameraViewModel> {
        CameraViewModel.CameraViewmodelFactory(CameraRepoImplem(CameraDataSource()))
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCameraBinding.bind(view)


        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "No se encontro app para abir la camara", Toast.LENGTH_SHORT).show()
        }


        binding.btnUploadPhoto.setOnClickListener{
            bitmap?.let { it1 ->
                viewmodel.uploadPhoto(it1,binding.textDescription.text.toString().trim()).observe(viewLifecycleOwner) {
                    when (it) {
                        is  com.kevin.courseApp.core.Result.Loading -> {
                            Toast.makeText(requireContext(), "Cargando foto", Toast.LENGTH_SHORT).show()

                        }
                        is com.kevin.courseApp.core.Result.Success -> {
                            Toast.makeText(requireContext(), "Subiendo foto", Toast.LENGTH_SHORT).show()

                        }
                        is com.kevin.courseApp.core.Result.Failure -> {
                            Toast.makeText(requireContext(),"${it.exception}" , Toast.LENGTH_SHORT).show()


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
            binding.imgAddPhoto.setImageBitmap(imageBitmap)
            bitmap = imageBitmap
        }
    }


    fun tomarFoto() {
        val takePictureIntent = Intent(ACTION_IMAGE_CAPTURE)
        try {
            resultLauncher.launch(takePictureIntent)

        } catch (e: ActivityNotFoundException) {
         }
    }


    private fun uploadPictureStorage(bitmap: Bitmap) {

        val storageRef = FirebaseStorage.getInstance().reference
        val imageref = storageRef.child("imagenes/${UUID.randomUUID()}.jpg")
        val baos = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val uploadTask = imageref.putBytes(data)
        uploadTask.continueWithTask {
            if (!it.isSuccessful) {
                it.exception?.let {
                    throw it
                }
            }

            imageref.downloadUrl
        }.addOnCompleteListener { acomodo ->
            if (acomodo.isSuccessful) {
                val downloadUrl = acomodo.result.toString()
                FirebaseFirestore.getInstance().collection("ciudades").document("LA")
                    .update(mapOf("imageurl" to downloadUrl))
             }
        }

    }


}
