package com.kevin.courseApp.ui.categoriaCursos

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kevin.courseApp.MainActivity
import com.kevin.courseApp.R
import com.kevin.courseApp.core.categoriaAdapter.CategoriasAdapter
import com.kevin.courseApp.data.model.Categoria
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.databinding.FragmentCategoriasBinding
import com.kevin.courseApp.databinding.ItemCategoriaBinding
import com.kevin.courseApp.ui.main.Detalles.CursoDetallesActivity
import com.kevin.courseApp.ui.main.favorites.FavoritosActivity
import com.kevin.courseApp.ui.main.youtubeVideoDetails.YoutubeVideoActvity

class CategoriasFragment : Fragment() {

    private lateinit var binding: FragmentCategoriasBinding // Reemplaza MyFragmentBinding con el nombre de tu clase de binding

    var seguir: Button? = null
    var epicDialog2: Dialog? = null
    var epicDialogTErminos: Dialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentCategoriasBinding.inflate(inflater, container, false) // Reemplaza MyFragmentBinding con el nombre de tu clase de binding


        /*  binding.imagenFav.setOnClickListener{
            fragmentFav()
        }

         */


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
        binding.gridView.adapter = adapter


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
        hamburgesa()



        binding.gridView.setOnItemClickListener { _, view, position, _ ->
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


        return binding.root

    }

    private fun fragmentFav() {

        val intent = Intent(activity, FavoritosActivity::class.java)
        startActivity(intent)

    }

    private fun hamburgesa() {


        // Establecer un oyente de eventos para los elementos del menú
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Manejar la selección del elemento del menú aquí
            when (menuItem.itemId) {
                R.id.nav_acerca -> {
                    mostrarSalir()
                    binding.drawerLayout.closeDrawers()

                    true
                }
                R.id.nav_fav -> {
                    fragmentFav()


                    true
                }
                R.id.navTerminos -> {
                    terminos()
                    binding.drawerLayout.closeDrawers()

                    true
                }
                /*
                R.id.salir -> {
                    FirebaseAuth.getInstance().signOut()
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)

                    true
                }

                 */
                R.id.nav_share -> {
                    compartir()
                    true
                }
                R.id.nav_profile -> {
                    formutodo("https://play.google.com/store/apps/details?id=com.app.formutodo&hl=es_MX&gl=US")
                    true
                }

                // Agrega más elementos del menú si es necesario
                else -> false
            }
        }

        // Agregar el botón de hamburguesa en la barra de herramientas

        val toggle = ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()




        // Agregar un listener al DrawerLayout
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerClosed(drawerView: View) {
                (requireActivity() as MainActivity).showSmooth()
            }

            override fun onDrawerOpened(drawerView: View) {
                // Lógica a realizar cuando se abre el Navigation Drawer
                (requireActivity() as MainActivity).hideSmoothBottomBar()
            }
        })



    }

    fun terminos() {
        epicDialogTErminos = Dialog(requireContext())
        epicDialogTErminos!!.setContentView( R.layout.terminos)
        seguir = epicDialogTErminos!!.findViewById( R.id.botonvamo) as Button
        seguir!!.setOnClickListener {
            epicDialogTErminos!!.dismiss()
        }

        epicDialogTErminos!!.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        epicDialogTErminos!!.show()

    }
    fun mostrarSalir() {
        epicDialog2 = Dialog(requireContext())
        epicDialog2!!.setContentView( R.layout.about2)
        seguir = epicDialog2!!.findViewById( R.id.botonvamo) as Button
        seguir!!.setOnClickListener {
            val url =
                "https://play.google.com/store/apps/details?id=com.kevin.courseApp"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        epicDialog2!!.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        epicDialog2!!.show()



    }

    private fun formutodo(enlace : String) {
        val url = enlace
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun compartir() {
        val compartir = Intent(Intent.ACTION_SEND)
        compartir.type = "text/plain"
        val mensaje =
            "SaberApp - Aprende todo en uno. \nApp completa con todo lo que un estudiante necesita. \nhttps://play.google.com/store/apps/details?id=com.app.formutodo"
        compartir.putExtra(Intent.EXTRA_SUBJECT, "Miles de cursos GRATIS")
        compartir.putExtra(Intent.EXTRA_TEXT, mensaje)
        startActivity(Intent.createChooser(compartir, "Compartir Via"))



    }
    override fun onResume() {
        super.onResume()
        binding.particleView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.particleView.pause()
    }
}
