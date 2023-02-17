package com.kevin.courseApp.ui.main

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.kevin.courseApp.R
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.core.adapterCurso.CursosAdapter
import com.kevin.courseApp.core.hide
import com.kevin.courseApp.core.show
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.data.model.Posts

import com.kevin.courseApp.data.remote.home.HomeScreenDataSource
import com.kevin.courseApp.databinding.FragmentHomeBinding
import com.kevin.courseApp.domain.home.HomeScreenRepoImplement
import com.kevin.courseApp.presentation.HomeScreenViewModel
import com.kevin.courseApp.presentation.HomeScreenViewModelFactory
import com.kevin.courseApp.ui.main.Detalles.CursoDetallesActivity
import com.kevin.courseApp.ui.main.adapter.HomeScreenAdapter
import com.kevin.courseApp.ui.main.adapter.onPostClickListener

class HomeFragment : Fragment(R.layout.fragment_home), onPostClickListener {
    private lateinit var binding: FragmentHomeBinding
    lateinit var epicdialog: Dialog

    private lateinit var database: DatabaseReference
    private lateinit var cursosAdapter: CursosAdapter

    private val viewmodel by viewModels<HomeScreenViewModel>
    {
    (HomeScreenViewModelFactory(
            HomeScreenRepoImplement(HomeScreenDataSource())
        ))
    }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            super.onViewCreated(view, savedInstanceState)
            binding = FragmentHomeBinding.bind(view)


            database = FirebaseDatabase.getInstance().reference.child("cursos")
            cursosAdapter = CursosAdapter(listOf())

            binding.recyclerViewCursos.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = cursosAdapter
            }



            // Agregar oyente de eventos para leer los cursos desde la base de datos
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val cursos = mutableListOf<Cursos>()
                    for (cursoSnapshot in dataSnapshot.children) {
                        val curso = cursoSnapshot.getValue(Cursos::class.java)
                        curso?.let {
                            cursos.add(it)
                        }
                    }
                    cursosAdapter.cursos = cursos
                    cursosAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, "Error al obtener cursos", databaseError.toException())
                }
            })

            // Configurar el oyente de clics del adaptador

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



        }


            //    binding.rvHome.adapter = HomeScreenAdapter(postlist = postlist)


    private fun esconderAnimacion() {


        epicdialog.dismiss()
    }

    private fun mostrarAnimacion() {


        epicdialog = Dialog(requireContext())
        epicdialog.setContentView(R.layout.progress_layout)
        epicdialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        epicdialog.setCanceledOnTouchOutside(false)
        epicdialog.show()

    }


    override fun onlikeBtnClick(post: Posts, liked: Boolean) {
        super.onlikeBtnClick(post, liked)

        viewmodel.registerLikeButtonState(post.id, liked).observe(viewLifecycleOwner){
            when(it){
                is  Result.Loading ->{
                    Toast.makeText(requireContext(),"PROGRESO",Toast.LENGTH_SHORT).show()

                }
                is  Result.Success ->{

                    Toast.makeText(requireContext(),"CHIDO",Toast.LENGTH_SHORT).show()


                }
                is  Result.Failure ->{

                    Toast.makeText(requireContext(),"Ocurrio un error ${it.exception}",Toast.LENGTH_SHORT).show()

                }
            }
        }

    }



}
