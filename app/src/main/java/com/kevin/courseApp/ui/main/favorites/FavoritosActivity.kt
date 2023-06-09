package com.kevin.courseApp.ui.main.favorites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.core.adapterCurso.CursoGuardadoAdapter
import com.kevin.courseApp.core.adapterCurso.CursosAdapter
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.data.remote.home.CursosDataSource
import com.kevin.courseApp.databinding.ActivityCategoryDetalleBinding
import com.kevin.courseApp.databinding.ActivityFavoritosBinding
import com.kevin.courseApp.domain.home.CursosImplement
import com.kevin.courseApp.presentation.HomeScreenViewModel
import com.kevin.courseApp.presentation.HomeScreenViewModelFactory
import com.kevin.courseApp.ui.main.Detalles.CursoDetallesActivity
import com.kevin.courseApp.utils.animacionProgress


class FavoritosActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var cursosAdapter: CursoGuardadoAdapter
    private lateinit var binding: ActivityFavoritosBinding
    private lateinit var viewModel: HomeScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityFavoritosBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        database = FirebaseDatabase.getInstance().reference


        binding.recyclerView2.layoutManager = LinearLayoutManager(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        cursosAdapter = CursoGuardadoAdapter(listOf())
        binding.recyclerView2.apply {
            layoutManager = LinearLayoutManager(this@FavoritosActivity)
            adapter = cursosAdapter
        }


        viewModel = ViewModelProvider(
            this,
            HomeScreenViewModelFactory(CursosImplement(CursosDataSource()))
        )[HomeScreenViewModel::class.java]



        viewModel.getFavoritos().observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val cursos = result.data
                    cursosAdapter.cursos = cursos
                    cursosAdapter.notifyDataSetChanged()
                    animacionProgress.esconderCarga()


                }
                is Result.Failure -> {
                    animacionProgress.esconderCarga()
                    Log.e("TAG", "Error al obtener cursos", result.exception)
                }
                else -> {}
            }
        }

//llamado a firebase

        cursosAdapter.setOnItemClickListener(object : CursoGuardadoAdapter.OnItemClickListener {
            override fun onItemClick(curso: Cursos) {
                val intent = Intent(this@FavoritosActivity, CursoDetallesActivity::class.java).apply {
                    putExtra("titulo", curso.titulo)
                    putExtra("descripcion", curso.descripcion)
                    putExtra("imagenUrl", curso.imagenUrl)
                    putExtra("enlace", curso.enlace)
                    putExtra("valoracion", curso.valoracion)
                    putExtra("duracion", curso.duracion)
                    putExtra("idioma", curso.idioma)
                    putExtra("estudiantes", curso.estudiantes)
                    putExtra("imagenFondo", curso.imagenFondo)
                    putExtra("empresa", curso.empresa)
                    putExtra("creador", curso.creador)

                }
                startActivity(intent)
            }
        })
        animacionProgress.mostrarCarga(this)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}