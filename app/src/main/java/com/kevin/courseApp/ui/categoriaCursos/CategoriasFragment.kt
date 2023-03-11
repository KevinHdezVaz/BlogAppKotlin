package com.kevin.courseApp.ui.categoriaCursos

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kevin.courseApp.R
import com.kevin.courseApp.core.categoriaAdapter.CategoriasAdapter
import com.kevin.courseApp.data.model.Categoria
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.databinding.FragmentCategoriasBinding
class CategoriasFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_categorias, container, false)
        val gridView = view.findViewById<GridView>(R.id.gridView)

        val data = listOf(
            Categoria(R.drawable.chevo, "Programación", "as"),
            Categoria(R.drawable.aplicacion, "Idiomas", "s")
        )

        val adapter = CategoriasAdapter(requireContext(), data)
        gridView.adapter = adapter

        gridView.setOnItemClickListener { _, view, position, _ ->

           findNavController().navigate(R.id.action_categoriasFragment_to_categoria_Detalles)
        }




        return view
    }
}
