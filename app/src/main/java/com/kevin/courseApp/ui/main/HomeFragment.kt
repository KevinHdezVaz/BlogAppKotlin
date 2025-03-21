package com.kevin.courseApp.ui.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.kevin.courseApp.MainActivity
import com.kevin.courseApp.R
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.core.adapterCurso.AdapterTools
import com.kevin.courseApp.core.adapterCurso.CursosAdapter
import com.kevin.courseApp.core.adapterCurso.CursosAdapterNews
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.data.model.Item
import com.kevin.courseApp.data.remote.home.CursosDataSource
import com.kevin.courseApp.databinding.FragmentHomeBinding
import com.kevin.courseApp.domain.home.CursosImplement
import com.kevin.courseApp.presentation.HomeScreenViewModel
import com.kevin.courseApp.presentation.HomeScreenViewModelFactory
import com.kevin.courseApp.ui.main.Detalles.CursoDetallesActivity
import com.kevin.courseApp.ui.main.favorites.FavoritosActivity
import com.kevin.courseApp.ui.main.incrementa_habilidades.FormutodoActivity
import com.kevin.courseApp.ui.openia.Menu.MainActivityOpen
import com.kevin.courseApp.utils.animacionProgress
import com.kevin.courseApp.utils.animacionProgress.Companion.esconderCarga
import com.kevin.courseApp.utils.quotes
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment(R.layout.fragment_home)   {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var cursosAdapter: CursosAdapter
    private lateinit var cursosAdapternew: CursosAdapterNews
    private lateinit var cursoTools: AdapterTools

    var epicDialog2: Dialog? = null
    var epicDialogTErminos: Dialog? = null
    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var viewModelnews: HomeScreenViewModel

    var seguir: Button? = null
    private var currentIndex = 0
    private val delayMillis = 8000L // Intervalo de cambio en milisegundos

    private val handler = Handler()
    private val runnable = Runnable {
        showNextQuote()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)


        mostrarCitas()
        esconderTextos()

//para salir
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
             requireActivity().finish()
        }


        binding.imagenFav.setOnClickListener{
            fragmentFav()
        }




        cursosAdapter = CursosAdapter(listOf())
        cursosAdapternew = CursosAdapterNews(listOf())
        cursoTools = AdapterTools()


        //agregar esa vaina al adaptadro


        binding.recyclerviewChatgpt.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false)
            adapter = cursoTools
        }


        // Agregar datos de ejemplo
        val item1 = Item(  R.drawable.herramienta )
        val item2 = Item(  R.drawable.herramienta2 )
        val item3 = Item(  R.drawable.ingenieria )
        cursoTools.addItem(item1)
        cursoTools.addItem(item2)
        cursoTools.addItem(item3)

        cursoTools.setOnItemClickListener { item ->
            when (item) {
                item1 -> {
                    val intent = Intent(requireContext(), MainActivityOpen::class.java)
                    requireActivity().startActivity(intent)

                }
                item2 -> {
                    val intent = Intent(requireContext(), FormutodoActivity::class.java)
                    requireActivity().startActivity(intent)

                }
                item3 -> {
                    // Acción cuando se hace clic en el item3
                }
            }
        }


        //send to all courses
        binding.texto4.setOnClickListener{

            findNavController().navigate(R.id.action_homeFragment_to_allCoursesFragment)

        }
        binding.texto5.setOnClickListener{

            findNavController().navigate(R.id.action_homeFragment_to_masEstudianteFragment)

        }


        binding.recyclerViewCursos.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false)

            adapter = cursosAdapter
        }


        binding.recyclerViewCursosAgregadosReciente.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false)
            adapter = cursosAdapternew
        }


//navigationview

        viewModel = ViewModelProvider(
            this,
            HomeScreenViewModelFactory(CursosImplement(CursosDataSource()))
        )[HomeScreenViewModel::class.java]

        viewModelnews = ViewModelProvider(
            this,
            HomeScreenViewModelFactory(CursosImplement(CursosDataSource()))
        )[HomeScreenViewModel::class.java]

        imagenesCarousel()
        hamburgesa()

        viewModel.getCursos().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val cursos = result.data
                    cursosAdapter.cursos = cursos
                    cursosAdapter.notifyDataSetChanged()
                    esconderCarga()
                    mostrarTextos()

                }
                is Result.Failure -> {
                    esconderCarga()

                    Log.e(TAG, "Error al obtener cursos", result.exception)
                }
                else -> {}
            }
        }
        viewModelnews.getCoursesNew().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val cursos = result.data
                    cursosAdapternew.cursos = cursos
                    cursosAdapternew.notifyDataSetChanged()
                    // esconderCarga()

                }
                is Result.Failure -> {
                    // esconderCarga()
                    Log.e(TAG, "Error al obtener cursos", result.exception)
                }
                else -> {}
            }
        }

        //adpatador de herrramientas


            cursosAdapter.setOnItemClickListener(object : CursosAdapter.OnItemClickListener {
                override fun onItemClick(curso: Cursos) {
                    val intent = Intent(activity, CursoDetallesActivity::class.java).apply {
                        putExtra("titulo", curso.titulo)
                        putExtra("descripcion", curso.descripcion)
                        putExtra("imagenUrl", curso.imagenUrl)
                        putExtra("enlace", curso.enlace)
                        putExtra("valoracion", curso.valoracion)
                        putExtra("duracion", curso.duracion)
                        putExtra("idioma", curso.idioma)
                        putExtra("estudiantes", curso.estudiantes)
                        putExtra("imagenFondo", curso.imagenFondo)
                        putExtra("empresa", curso.empresa)
                        putExtra("creador", curso.creador)
                    }
                    startActivity(intent)
                }
            })

        cursosAdapternew.setOnItemClickListener(object : CursosAdapterNews.OnItemClickListener {
            override fun onItemClick(curso: Cursos) {
                val intent = Intent(activity, CursoDetallesActivity::class.java).apply {
                    putExtra("titulo", curso.titulo)
                    putExtra("descripcion", curso.descripcion)
                    putExtra("imagenUrl", curso.imagenUrl)
                    putExtra("enlace", curso.enlace)
                    putExtra("valoracion", curso.valoracion)
                    putExtra("duracion", curso.duracion)
                    putExtra("idioma", curso.idioma)
                    putExtra("estudiantes", curso.estudiantes)
                    putExtra("imagenFondo", curso.imagenFondo)
                    putExtra("empresa", curso.empresa)
                    putExtra("creador", curso.creador)
                }
                startActivity(intent)
            }
        })


        animacionProgress.mostrarCarga(requireContext() )

    }

    private fun esconderTextos() {

        binding.texto1.visibility = View.GONE
        binding.texto2.visibility= View.GONE
        binding.texto3.visibility = View.GONE
        binding.texto4.visibility = View.GONE
        binding.texto5.visibility = View.GONE
        binding.recyclerviewChatgpt.visibility = View.GONE
    }

    private fun mostrarTextos() {

        binding.texto1.visibility = View.VISIBLE
        binding.texto2.visibility= View.VISIBLE
        binding.texto3.visibility = View.VISIBLE
        binding.texto4.visibility = View.VISIBLE
        binding.recyclerviewChatgpt.visibility = View.VISIBLE
        binding.texto5.visibility = View.VISIBLE
    }


    private fun mostrarCitas() {
        startQuoteRotation()

    }
    private fun startQuoteRotation() {
        handler.postDelayed(runnable, delayMillis)
    }


    private fun showNextQuote() {
        val quotes = quotes()
        val random = Random()
        val randomIndex = random.nextInt(quotes.quotesList.size)
        binding.textQuote.text = quotes.quotesList[randomIndex]
        handler.removeCallbacks(runnable)
        handler.postDelayed(runnable, delayMillis)
    }




    override fun onDestroy() {
        super.onDestroy()
        // Detener la rotación de citas al destruir la actividad
        handler.removeCallbacks(runnable)
    }
    private fun fragmentFav() {

        val intent = Intent(activity, FavoritosActivity::class.java)
        startActivity(intent)

    }


    private fun hamburgesa() {

// Obtener referencia al NavigationView
        val navigationView = binding.navigationView

// Obtener referencia al TextView en el header
        val headerView = navigationView.getHeaderView(0)
        val txtUsuario = headerView.findViewById<TextView>(R.id.textUsuario)

// Obtener la información del usuario actual de Firebase
        val user = FirebaseAuth.getInstance().currentUser

// Verificar si el usuario ha iniciado sesión y establecer el correo electrónico o el usuario en el TextView correspondiente
        if (user != null) {
            txtUsuario.text = user.email ?:  "Usuario"
        } else {
            txtUsuario.text = "Usuario"
        }

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

                R.id.salir -> {
                    FirebaseAuth.getInstance().signOut()
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)

                    true
                }


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


    private fun imagenesCarousel() {

        val imageList = ArrayList<SlideModel>() // Create image list


        imageList.add(SlideModel(R.drawable.farmatdo))
        imageList.add(SlideModel(R.drawable.formaa))

        imageList.add(SlideModel(R.drawable.soluuu))

        //binding.imageSlider.setImageList(imageList)
        /*binding.imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
              when(position){
                  0 -> formutodo("https://play.google.com/store/apps/details?id=com.app.formutodo&hl=es_MX&gl=US")
                  //en uno, mandarlo a la categoria de ingles
                  1-> formutodo("https://play.google.com/store/apps/details?id=com.kevin.courseApp")
                  2-> formutodo("https://play.google.com/store/apps/details?id=com.app.formutodo&hl=es_MX&gl=US")

              }
            }
        })    */
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
