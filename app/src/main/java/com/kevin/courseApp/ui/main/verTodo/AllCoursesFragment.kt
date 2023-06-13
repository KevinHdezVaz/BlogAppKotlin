package com.kevin.courseApp.ui.main.verTodo

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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.kevin.courseApp.R
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.core.adapterCurso.CursosAdapter
import com.kevin.courseApp.core.adapterCurso.CursosAdapterAll
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.data.remote.home.CursosDataSource
import com.kevin.courseApp.databinding.FragmentAllCoursesBinding
import com.kevin.courseApp.databinding.FragmentBuscarBinding
import com.kevin.courseApp.domain.home.CursosImplement
import com.kevin.courseApp.presentation.HomeScreenViewModel
import com.kevin.courseApp.presentation.HomeScreenViewModelFactory
import com.kevin.courseApp.ui.main.Detalles.CursoDetallesActivity
import com.kevin.courseApp.utils.animacionProgress


class AllCoursesFragment : Fragment(R.layout.fragment_all_courses) {

    private lateinit var binding: FragmentAllCoursesBinding
    private lateinit var database: DatabaseReference
    private lateinit var cursosAdapter: CursosAdapterAll

    private lateinit var viewModel: HomeScreenViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAllCoursesBinding.bind(view)

        database = FirebaseDatabase.getInstance().reference


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }



        cursosAdapter = CursosAdapterAll(listOf())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cursosAdapter
        }


        viewModel = ViewModelProvider(
            this,
            HomeScreenViewModelFactory(CursosImplement(CursosDataSource()))
        )[HomeScreenViewModel::class.java]



        viewModel.getCursosAll().observe(viewLifecycleOwner) { result ->
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

        cursosAdapter.setOnItemClickListener(object : CursosAdapterAll.OnItemClickListener {
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
                    putExtra("imagenFondo", curso.imagenFondo)
                    putExtra("empresa", curso.empresa)
                    putExtra("creador", curso.creador)
                }
                startActivity(intent)
            }
        })
        animacionProgress.mostrarCarga(requireContext() )
        //HomeFragment.mostrarCarga(requireContext(), "loading.json")

    }





}
