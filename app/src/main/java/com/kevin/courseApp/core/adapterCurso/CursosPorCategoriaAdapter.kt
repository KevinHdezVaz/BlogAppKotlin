package com.kevin.courseApp.core.adapterCurso

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kevin.courseApp.R
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.data.model.CursosPorCategoria


class CursosPorCategoriaAdapter(private val cursosPorCategoria: List<CursosPorCategoria>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_CATEGORIA = 1
    private val VIEW_TYPE_CURSO = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CATEGORIA -> CategoriaViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_categoria_curso, parent, false)
            )
            else -> CursoViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_curso, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_CATEGORIA -> {
                val categoriaViewHolder = holder as CategoriaViewHolder
                val categoria = cursosPorCategoria[position].categoria
                categoriaViewHolder.bind(categoria)
            }
            VIEW_TYPE_CURSO -> {
                val cursoViewHolder = holder as CursoViewHolder
                val curso = cursosPorCategoria[position].cursos[position - 1]
                cursoViewHolder.bind(curso)
            }
        }
    }

    override fun getItemCount(): Int {
        var count = 0
        for (cursos in cursosPorCategoria) {
            count += cursos.cursos.size + 1
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 || cursosPorCategoria[position - 1].categoria != cursosPorCategoria[position].categoria) {
            VIEW_TYPE_CATEGORIA
        } else {
            VIEW_TYPE_CURSO
        }
    }

    inner class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoriaTextView: TextView = itemView.findViewById(R.id.categoria_text_view)

        fun bind(categoria: String) {
            categoriaTextView.text = categoria
        }
    }

    inner class CursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tituloCurso: TextView = itemView.findViewById(R.id.titulo_curso)
        private val descripcionCurso: TextView = itemView.findViewById(R.id.descripcion_curso)
        private val imagenCurso: ImageView = itemView.findViewById(R.id.imagen_curso)

        fun bind(curso: Cursos) {
            tituloCurso.text = curso.titulo
            descripcionCurso.text = curso.descripcion
            Glide.with(itemView.context).load(curso.imagenUrl).into(imagenCurso)
        }
    }
}
