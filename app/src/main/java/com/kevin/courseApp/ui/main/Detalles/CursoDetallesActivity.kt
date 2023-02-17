package com.kevin.courseApp.ui.main.Detalles

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.kevin.courseApp.R
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.databinding.ActivityCursoDetallesBinding

class CursoDetallesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCursoDetallesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCursoDetallesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Obtener los datos del curso desde el intent
        val titulo = intent.getStringExtra("titulo")
        val descripcion = intent.getStringExtra("descripcion")
        val imagenUrl = intent.getStringExtra("imagenUrl")
        val enlace = intent.getStringExtra("enlace")

        // Mostrar los datos del curso en la vista
        binding.tituloCurso.text = titulo
        binding.descripcionCurso.text = descripcion
        Glide.with(this).load(imagenUrl).into(binding.imagenCurso)



        // Configurar el botón para abrir el enlace en un WebView
        binding.enlaceButton.setOnClickListener {
            val intent = Intent(this, WebViewCurso::class.java)
            intent.putExtra("enlace", enlace)
            intent.putExtra("titulo", titulo)

            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
