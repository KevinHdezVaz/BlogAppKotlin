package com.kevin.courseApp.ui.main.buscar


import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import com.kevin.courseApp.databinding.FragmentBuscarBinding
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.kevin.courseApp.R
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.core.adapterCurso.BuscarAdapter
import com.kevin.courseApp.core.adapterCurso.CursosAdapter
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.data.remote.home.CursosDataSource
import com.kevin.courseApp.databinding.FragmentHomeBinding
import com.kevin.courseApp.domain.home.CursosImplement
import com.kevin.courseApp.presentation.HomeScreenViewModel
import com.kevin.courseApp.presentation.HomeScreenViewModelFactory
import com.kevin.courseApp.ui.main.Detalles.CursoDetallesActivity
import com.kevin.courseApp.ui.main.HomeFragment
import com.kevin.courseApp.utils.animacionProgress

class BuscarFragment : Fragment(R.layout.fragment_buscar) {


    private lateinit var binding: FragmentBuscarBinding
    private lateinit var database: DatabaseReference
    private lateinit var cursosAdapter: CursosAdapter

    private lateinit var viewModel: HomeScreenViewModel



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBuscarBinding.bind(view)

        database = FirebaseDatabase.getInstance().reference


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())




        cursosAdapter = CursosAdapter(listOf())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cursosAdapter
        }


        viewModel = ViewModelProvider(
            this,
            HomeScreenViewModelFactory(CursosImplement(CursosDataSource()))
        )[HomeScreenViewModel::class.java]

        binding.searchView.postDelayed({
            binding.searchView.isIconified = false
            binding.searchView.requestFocus()
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    searchCursos(newText)
                }
                return true
            }
        })





        viewModel.getCursos().observe(viewLifecycleOwner) { result ->
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
                }
                startActivity(intent)
            }
        })
        animacionProgress.mostrarCarga(requireContext() )
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
                Log.w(TAG, "onCancelled", databaseError.toException())
            }
        })
    }



}
