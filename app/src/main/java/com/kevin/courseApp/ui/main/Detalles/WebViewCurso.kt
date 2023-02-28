package com.kevin.courseApp.ui.main.Detalles

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.kevin.courseApp.R
import com.kevin.courseApp.databinding.ActivityWebViewCursoBinding
import com.kevin.courseApp.utils.animacionProgress


class WebViewCurso : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewCursoBinding
        var enlace: String? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewCursoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Obtener el enlace del intent

        enlace = intent.getStringExtra("enlace")
        val titulo = intent.getStringExtra("titulo")

        // Configurar el WebView
        // Mostrar el ProgressBar mientras se cargan los datos

         animacionProgress.mostrarCarga(this)


        // Configurar el WebView
        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                    animacionProgress.esconderCarga()
            }
        }
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.loadUrl(enlace!!)
        binding.webview.settings.mixedContentMode



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
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.tre_barras, menu)
        val webItem = menu?.findItem(R.id.action_favorite)
        webItem?.setOnMenuItemClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(enlace)
            startActivity(openURL)
            true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {


                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
