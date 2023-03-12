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
import com.kevin.courseApp.ui.main.Detalles.CursoDetallesActivity

class CategoriasFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_categorias, container, false)
        val gridView = view.findViewById<GridView>(R.id.gridView)

        val data = listOf(
            Categoria(R.drawable.programm, "Programación", "as"),
            Categoria(R.drawable.idiomas, "Idiomas", "s"),
           Categoria(R.drawable.compu, "Computación", "as"),
            Categoria(R.drawable.presupuesto, "Finanzas", "s"),
           Categoria(R.drawable.diseno, "Diseño", "ss"),
            Categoria(R.drawable.marketing, "Marketing", "sx"),
            Categoria(R.drawable.ingenieria, "Ingeniería", "scx"),
             Categoria(R.drawable.amor, "Humanidades", "huma"),
            Categoria(R.drawable.arte, "Arte", "humca"),


            )

        val adapter = CategoriasAdapter(requireContext(), data)
        gridView.adapter = adapter

        gridView.setOnItemClickListener { _, view, position, _ ->
            val bundle = Bundle()
            when (position) {
                0 -> bundle.putString("categoria", "Programación")
                1 -> bundle.putString("categoria", "Idiomas")
                2 -> bundle.putString("categoria", "Computación")
                3 -> bundle.putString("categoria", "Finanzas")
                4 -> bundle.putString("categoria", "Diseño")
                5 -> bundle.putString("categoria", "Marketing")
                6 -> bundle.putString("categoria", "Ingeniería")
                7 -> bundle.putString("categoria", "Humanidades")
                8 -> bundle.putString("categoria", "Arte")
            }
            findNavController().navigate(R.id.action_categoriasFragment_to_categoria_Detalles, bundle)
        }





        return view
    }
}
