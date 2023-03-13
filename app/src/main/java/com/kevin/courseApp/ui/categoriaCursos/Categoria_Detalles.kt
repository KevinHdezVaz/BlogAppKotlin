package com.kevin.courseApp.ui.categoriaCursos

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.kevin.courseApp.R
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.core.adapterCurso.CursosAdapter
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.data.remote.home.CursosDataSource
import com.kevin.courseApp.databinding.FragmentBuscarBinding
import com.kevin.courseApp.databinding.FragmentCategoriaDetallesBinding
import com.kevin.courseApp.domain.home.CursosImplement
import com.kevin.courseApp.presentation.HomeScreenViewModel
import com.kevin.courseApp.presentation.HomeScreenViewModelFactory
import com.kevin.courseApp.ui.main.Detalles.CursoDetallesActivity
import com.kevin.courseApp.utils.animacionProgress

class Categoria_Detalles : Fragment() {

    private lateinit var binding: FragmentCategoriaDetallesBinding
    private lateinit var database: DatabaseReference
    private lateinit var cursosAdapter: CursosAdapter
    private lateinit var viewModel: HomeScreenViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCategoriaDetallesBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        cursosAdapter = CursosAdapter(listOf())
        binding.recyclerView.adapter = cursosAdapter

        viewModel = ViewModelProvider(this, HomeScreenViewModelFactory(CursosImplement(CursosDataSource())))[HomeScreenViewModel::class.java]

        //
        val categoria = arguments?.getString("categoria")

        viewModel.getCursosFiltrados(categoria!!).observe(viewLifecycleOwner) { result ->
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

        cursosAdapter.setOnItemClickListener(object : CursosAdapter.OnItemClickListener {
            override fun onItemClick(curso: Cursos) {
                val intent = Intent(activity, CursoDetallesActivity::class.java).apply {
                    putExtra("titulo", curso.titulo)
                    putExtra("descripcion", curso.descripcion)
                    putExtra("imagenUrl", curso.imagenUrl)
                    putExtra("enlace", curso.enlace)
                    putExtra("valoracion", curso.valoracion)
                    putExtra("duracion", curso.duracion)
                    putExtra("idioma", curso.idioma)
                    putExtra("estudiantes", curso.estudiantes)
                }
                startActivity(intent)
            }
        })

        animacionProgress.mostrarCarga(requireContext())

        return binding.root
    }
}
