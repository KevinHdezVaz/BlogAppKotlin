package com.kevin.courseApp.ui.categoriaCursos

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.kevin.courseApp.R
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.core.adapterCurso.CursosAdapter
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.data.remote.home.CursosDataSource
import com.kevin.courseApp.databinding.ActivityCategoryDetalleBinding
import com.kevin.courseApp.domain.home.CursosImplement
import com.kevin.courseApp.presentation.HomeScreenViewModel
import com.kevin.courseApp.presentation.HomeScreenViewModelFactory
import com.kevin.courseApp.ui.main.Detalles.CursoDetallesActivity
import com.kevin.courseApp.utils.animacionProgress


class CategoryDetalle_Activity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var cursosAdapter: CursosAdapter
    private lateinit var binding: ActivityCategoryDetalleBinding
    private lateinit var viewModel: HomeScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityCategoryDetalleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /***
         * agrega un metodo que los filtre por categorias
         * ejem: anterior pantalla : "programacion"
         * en esta pantalla, llamas a los cursos y los filtras por "programacion"
         *
         */

        database = FirebaseDatabase.getInstance().reference


        binding.recyclerView.layoutManager = LinearLayoutManager(this)




        cursosAdapter = CursosAdapter(listOf())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CategoryDetalle_Activity)
            adapter = cursosAdapter
        }


        viewModel = ViewModelProvider(
            this,
            HomeScreenViewModelFactory(CursosImplement(CursosDataSource()))
        )[HomeScreenViewModel::class.java]



        viewModel.getCursos().observe(this) { result ->
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

        cursosAdapter.setOnItemClickListener(object : CursosAdapter.OnItemClickListener {
            override fun onItemClick(curso: Cursos) {
                val intent = Intent(this@CategoryDetalle_Activity, CursoDetallesActivity::class.java).apply {
                    putExtra("titulo", curso.titulo)
                    putExtra("descripcion", curso.descripcion)
                    putExtra("imagenUrl", curso.imagenUrl)
                    putExtra("enlace", curso.enlace)
                }
                startActivity(intent)
            }
        })
        animacionProgress.mostrarCarga(this)
        //HomeFragment.mostrarCarga(requireContext(), "loading.json")

    }





    private fun searchCursos(titulo: String) {
        val cursos = ArrayList<Cursos>()

        val query = FirebaseDatabase.getInstance().reference.child("cursos")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (cursoSnapshot in dataSnapshot.children) {
                    val curso = cursoSnapshot.getValue(Cursos::class.java)
                    if (curso != null && curso.titulo.contains(titulo,ignoreCase = true)) {
                        cursos.add(curso)
                    }
                }


                cursosAdapter.cursos = cursos
                cursosAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "onCancelled", databaseError.toException())
            }
        })
    }



}