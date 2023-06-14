package com.kevin.courseApp.ui.main.incrementa_habilidades

 import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
 import androidx.navigation.fragment.findNavController
 import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.kevin.courseApp.R
import com.kevin.courseApp.databinding.ActivityFormutodoBinding
import com.kevin.courseApp.databinding.ItemCustomFixedSizeLayout1Binding
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.listener.CarouselOnScrollListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage


class FormutodoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormutodoBinding
    private lateinit var context: Context
    var epicDialogformu: Dialog? = null
    var btn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormutodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


//si no es la primera vez
        if (isFirstTime(this)) {
            mostrarSalir()
        }



        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            binding.backkk.setBackgroundResource(R.drawable.fondoform)

        } else {
            binding.backkk.setBackgroundResource(R.drawable.fondofor_light)
         }
        binding.carousel3.registerLifecycle(lifecycle)

        // Custom view




        val listThree = mutableListOf<CarouselItem>()

        for (item in DataSet.three) {
            listThree.add(
                CarouselItem(
                    imageUrl = item.first,
                    caption = item.second,
                )
            )
        }

        binding.carousel3.setData(listThree)

        binding.customCaption.isSelected = true



        binding.carousel3.carouselListener = object : CarouselListener {
            override fun onCreateViewHolder(
                layoutInflater: LayoutInflater,
                parent: ViewGroup
            ): ViewBinding {
                return ItemCustomFixedSizeLayout1Binding.inflate(layoutInflater, parent, false)
            }


            override fun onBindViewHolder(
                binding: ViewBinding,
                item: CarouselItem,
                position: Int
            ) {
                val currentBinding = binding as ItemCustomFixedSizeLayout1Binding
                currentBinding.imageView.apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    setImage(item, org.imaginativeworld.whynotimagecarousel.R.drawable.carousel_default_placeholder)
                    setOnClickListener {
                        // Acción al dar click en el elemento del carousel
                        when (position) {
                            0 -> {
                            abrirEnlace("https://play.google.com/store/apps/details?id=com.app.formutodo")
                            }
                            1 -> {
                                abrirEnlace("https://play.google.com/store/apps/details?id=com.app.formutodo")

                            }
                            // ... Continuar con más casos para otras posiciones del carousel
                            else -> {
                                abrirEnlace("https://play.google.com/store/apps/details?id=com.app.formutodo")

                            }
                        }
                    }
                }
            }



        }


        binding.carousel3.onScrollListener = object : CarouselOnScrollListener {

            @SuppressLint("SetTextI18n")
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int,
                position: Int,
                carouselItem: CarouselItem?
            ) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    carouselItem?.apply {
                        binding.customCaption.text = caption

                        if (position == 0) {
                            binding.descripcionTV.text = "Formutodo esta dentro de las mejores apps de Educación en Play Store."
                        } else if (position == 1) {
                            binding.descripcionTV.text = "Formulas Matematicas, Fisicas y Quimicas GRATIS Y SIN CONEXIÓN A INTERNET."
                        }else if (position ==2) {
                            binding.descripcionTV.text = "Herramientas que te facilitan la resolución de tareas."
                        }
                        else if (position == 3) {
                            binding.descripcionTV.text = "Juegos interesantes para mejorar a razonar y aprender de manera divertida."
                        }

                    }
                }
            }

            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int,
                position: Int,
                carouselItem: CarouselItem?
            ) {
                // ...
            }
        }


        // Custom navigation
        binding.btnGotoPrevious.setOnClickListener {
            binding.carousel3.previous()
        }

        binding.btnGotoNext.setOnClickListener {
            binding.carousel3.next()
        }
    }

    fun mostrarSalir() {
        epicDialogformu = Dialog(this)
        epicDialogformu!!.setContentView( R.layout.about_formu)
        btn = epicDialogformu!!.findViewById( R.id.botonvamo) as Button
        btn!!.setOnClickListener {
           epicDialogformu!!.dismiss()
        }

        val imageView = epicDialogformu!!.findViewById(R.id.giff) as ImageView
        val resourceId = R.raw.gif_formutodo
        Glide.with(applicationContext).load(resourceId).into(imageView)


        epicDialogformu!!.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        epicDialogformu!!.show()

    }
    fun abrirEnlace(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun isFirstTime(context: Context): Boolean {
        val preferences = context.getSharedPreferences("myPreferencesFormu",  MODE_PRIVATE)
        val ranBefore = preferences.getBoolean("RanBeforefor", false)
        if (!ranBefore) {
            // first time
            val editor = preferences.edit()
            editor.putBoolean("RanBeforefor", true)
            editor.apply()
        }
        return !ranBefore
    }
}

