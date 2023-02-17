package com.kevin.courseApp.ui.main.Detalles

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import com.kevin.courseApp.R
import com.kevin.courseApp.databinding.ActivityWebViewCursoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WebViewCurso : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewCursoBinding


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewCursoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Obtener el enlace del intent

        val enlace = intent.getStringExtra("enlace")
        val titulo = intent.getStringExtra("titulo")

        // Configurar el WebView
        binding.webview.webViewClient = WebViewClient()
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.loadUrl(enlace!!)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        // Agregar botón de "volver" a la Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(titulo)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24) // Reemplazar con el icono de "volver" deseado

        // Configurar el comportamiento del botón de "volver"
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }


            }

}
