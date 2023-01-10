package com.kevin.blogappkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kevin.blogappkotlin.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*
          resultLauncher = registerForActivityResult(
            ActivityResultContracts
                .StartActivityForResult()
        ){
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                val imageBitmap = data?.extras?.get("data") as Bitmap
                binding.imageView.setImageBitmap(imageBitmap)
                uploadPictureStorage(imageBitmap)
            }
        }
         */
    }


    fun tomarFoto() {
        val takePictureIntent = Intent(ACTION_IMAGE_CAPTURE)
        try {
            resultLauncher.launch(takePictureIntent)

        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this@MainActivity, "no se encontro camrara", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@MainActivity, downloadUrl, Toast.LENGTH_SHORT).show()
            }
        }

    }


}
