package com.kevin.courseApp.presentation.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kevin.courseApp.data.model.Categoria
import com.kevin.courseApp.domain.home.CursosRepo
class CategoriasViewModel : ViewModel() {
    private val _categorias = MutableLiveData<List<Categoria>>()
    val categorias: LiveData<List<Categoria>> = _categorias

    init {
        obtenerCategorias()
    }

    private fun obtenerCategorias() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("cursos")

        val categoriasSet = mutableSetOf<String>()
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (cursoSnapshot in dataSnapshot.children) {
                    val categoria = cursoSnapshot.child("categoria").getValue(String::class.java)
                    categoria?.let {
                        categoriasSet.add(it)
                    }
                }

                val categoriasList = categoriasSet.map { Categoria(it) }
                _categorias.value = categoriasList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(TAG, "Error al obtener categorías", databaseError.toException())
            }
        })
    }
}
