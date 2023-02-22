package com.kevin.courseApp.ui.categoriaCursos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kevin.courseApp.core.categoriaAdapter.CategoriasAdapter
import com.kevin.courseApp.databinding.FragmentCategorias2Binding
import com.kevin.courseApp.presentation.home.CategoriasViewModel

class CategoriasFragment : Fragment() {

    private lateinit var binding: FragmentCategorias2Binding
    private lateinit var viewModel: CategoriasViewModel
    private lateinit var categoriasAdapter: CategoriasAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCategorias2Binding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(CategoriasViewModel::class.java)

        binding.gridViewCategorias.apply {
             adapter = categoriasAdapter
        }

        viewModel.categorias.observe(viewLifecycleOwner) { categorias ->
             categoriasAdapter.notifyDataSetChanged()
        }

        return binding.root
    }
}
