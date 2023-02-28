package com.kevin.courseApp.ui.categoriaCursos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kevin.courseApp.R
import com.kevin.courseApp.core.categoriaAdapter.CategoriasAdapter
import com.kevin.courseApp.data.model.Categoria
import com.kevin.courseApp.databinding.FragmentCategoriasBinding

class CategoriasFragment : Fragment(R.layout.fragment_categorias)  {

    private var  binding: FragmentCategoriasBinding? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         val categories = ArrayList<String>()
        categories.add("Categoría 1")
        categories.add("Categoría 2")
        categories.add("Categoría 3")
        categories.add("Categoría 4")
        categories.add("Categoría 5")
        val adapter = CategoriasAdapter(requireContext(), categories)
        binding?.gridview?.adapter = adapter



    }

    }

