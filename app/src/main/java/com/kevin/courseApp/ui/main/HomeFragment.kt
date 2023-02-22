package com.kevin.courseApp.ui.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
    import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.*
import com.kevin.courseApp.R
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.core.adapterCurso.CursosAdapter
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.data.remote.home.CursosDataSource
import com.kevin.courseApp.databinding.FragmentHomeBinding
import com.kevin.courseApp.domain.home.CursosImplement
import com.kevin.courseApp.presentation.HomeScreenViewModel
import com.kevin.courseApp.presentation.HomeScreenViewModelFactory
import com.kevin.courseApp.ui.main.Detalles.CursoDetallesActivity

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

             mostrarCarga(requireContext())
        }

    private fun imagenesCarousel() {

        val imageList = ArrayList<SlideModel>() // Create image list


        imageList.add(SlideModel(R.drawable.farmatdo,  ))
        imageList.add(SlideModel(R.drawable.language,  ))



        binding.imageSlider.setImageList(imageList)
        binding.imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                // You can listen here
            }
        })
    }


    companion object {
        private const val TAG = "HomeFragment"

        private var epicDialog2: Dialog? = null
        @SuppressLint("SuspiciousIndentation")
        fun mostrarCarga(context: Context) {
         epicDialog2 = Dialog(context)
            epicDialog2!!.setContentView(R.layout.progress_layout)
            epicDialog2!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
             epicDialog2!!.setCanceledOnTouchOutside(false)
             epicDialog2!!.show()
        }

        fun esconderCarga() {

            epicDialog2!!.dismiss()

        }

    }
}
