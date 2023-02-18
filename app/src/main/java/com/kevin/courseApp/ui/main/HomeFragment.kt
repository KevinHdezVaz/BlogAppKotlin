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
import android.widget.Toast
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var epicDialog2 : Dialog
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


    companion object {
        private const val TAG = "HomeFragment"

        private var epicDialog2: Dialog? = null
        fun mostrarCarga(context: Context) {
            Companion.epicDialog2 = Dialog(context)
            Companion.epicDialog2!!.setContentView(R.layout.progress_layout)
            Companion.epicDialog2!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            Companion.epicDialog2!!.setCanceledOnTouchOutside(false)
            Companion.epicDialog2!!.show()
        }

        fun esconderCarga() {
            Companion.epicDialog2?.dismiss()
        }

    }
}
