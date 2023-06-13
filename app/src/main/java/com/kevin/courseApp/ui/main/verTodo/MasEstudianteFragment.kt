package com.kevin.courseApp.ui.main.verTodo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kevin.courseApp.R
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.core.adapterCurso.CursoAllEstudiantes
import com.kevin.courseApp.core.adapterCurso.CursosAdapterAll
import com.kevin.courseApp.core.adapterCurso.CursosAdapterNews
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.data.remote.home.CursosDataSource
import com.kevin.courseApp.databinding.FragmentAllCoursesBinding
import com.kevin.courseApp.databinding.FragmentMasEstudianteBinding
import com.kevin.courseApp.domain.home.CursosImplement
import com.kevin.courseApp.presentation.HomeScreenViewModel
import com.kevin.courseApp.presentation.HomeScreenViewModelFactory
import com.kevin.courseApp.ui.main.Detalles.CursoDetallesActivity
import com.kevin.courseApp.utils.animacionProgress

class MasEstudianteFragment : Fragment(R.layout.fragment_mas_estudiante) {

    private lateinit var binding: FragmentMasEstudianteBinding
    private lateinit var database: DatabaseReference
    private lateinit var cursosAdapter: CursoAllEstudiantes

    private lateinit var viewModel: HomeScreenViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMasEstudianteBinding.bind(view)

        database = FirebaseDatabase.getInstance().reference


        binding.recyclerViewestudiante.layoutManager = LinearLayoutManager(requireContext())


        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }



        cursosAdapter = CursoAllEstudiantes(listOf())
        binding.recyclerViewestudiante.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cursosAdapter
        }


        viewModel = ViewModelProvider(
            this,
            HomeScreenViewModelFactory(CursosImplement(CursosDataSource()))
        )[HomeScreenViewModel::class.java]



        viewModel.getCoursesNew().observe(viewLifecycleOwner) { result ->
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

        cursosAdapter.setOnItemClickListener(object : CursoAllEstudiantes.OnItemClickListener {
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
