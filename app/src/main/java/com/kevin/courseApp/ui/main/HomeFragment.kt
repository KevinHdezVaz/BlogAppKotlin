package com.kevin.courseApp.ui.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.*
import com.kevin.courseApp.R
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.core.adapterCurso.CursosAdapter
import com.kevin.courseApp.core.hide
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.data.remote.home.CursosDataSource
import com.kevin.courseApp.databinding.FragmentHomeBinding
import com.kevin.courseApp.domain.home.CursosImplement
import com.kevin.courseApp.presentation.HomeScreenViewModel
import com.kevin.courseApp.presentation.HomeScreenViewModelFactory
import com.kevin.courseApp.ui.main.Detalles.CursoDetallesActivity
import com.kevin.courseApp.utils.animacionProgress
import com.kevin.courseApp.utils.animacionProgress.Companion.esconderCarga

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var cursosAdapter: CursosAdapter
    private lateinit var viewModel: HomeScreenViewModel
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        cursosAdapter = CursosAdapter(listOf())
        binding.recyclerViewCursos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cursosAdapter
        }



        viewModel = ViewModelProvider(
            this,
            HomeScreenViewModelFactory(CursosImplement(CursosDataSource()))
        )[HomeScreenViewModel::class.java]

        imagenesCarousel()
        hamburgesa()






        viewModel.getCursos().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val cursos = result.data
                    cursosAdapter.cursos = cursos
                    cursosAdapter.notifyDataSetChanged()
                     esconderCarga()

                }
                is Result.Failure -> {
                     esconderCarga()

                    Log.e(TAG, "Error al obtener cursos", result.exception)
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
                    }
                    startActivity(intent)
                }
            })

        animacionProgress.mostrarCarga(requireContext() )

    }

    private fun hamburgesa() {


        // Establecer un oyente de eventos para los elementos del menú
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Manejar la selección del elemento del menú aquí
            when (menuItem.itemId) {
                R.id.menu_item_1 -> {
                    // Lógica para la selección del primer elemento del menú
                    true
                }
                R.id.menu_item_2 -> {
                    // Lógica para la selección del segundo elemento del menú
                    true
                }
                // Agrega más elementos del menú si es necesario
                else -> false
            }
        }

        // Agregar el botón de hamburguesa en la barra de herramientas

        val toggle = ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


    }

    private fun imagenesCarousel() {

        val imageList = ArrayList<SlideModel>() // Create image list


        imageList.add(SlideModel(R.drawable.farmatdo))
        imageList.add(SlideModel(R.drawable.language))

        binding.imageSlider.setImageList(imageList)
        binding.imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                // You can listen here
            }
        })
    }



}
