package com.kevin.courseApp.ui.main.Detalles

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kevin.courseApp.data.model.Cursos
import com.kevin.courseApp.databinding.ActivityCursoDetallesBinding


class CursoDetallesActivity : AppCompatActivity() {
    private var rewardedAd: RewardedAd? = null
    private final var TAG = "MainActivity"

    private var cursoSeleccionado: Cursos? = null
    private var isFav = false
    private var mAuth: FirebaseAuth? = null
    private var cursoID =""
    private lateinit var binding:  ActivityCursoDetallesBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCursoDetallesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // obtener la referencia del usuario actual


     val user = FirebaseAuth.getInstance().currentUser
        val userId = user?.uid

        // obtener la referencia al nodo Favoritos del usuario actual
        val favoritosRef = FirebaseDatabase.getInstance().getReference("usuarios").child(userId!!).child("cursosGuardados")

        // verificar si el curso ya está guardado en Firebase
        favoritosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (cursoSnapshot in snapshot.children) {
                    val curso = cursoSnapshot.getValue(Cursos::class.java)
                    if (curso?.titulo == intent.getStringExtra("titulo")) {
                        // el curso ya está guardado, deshabilitar el botón de guardado
                        binding.btnGuardar.isEnabled = false
                        binding.btnGuardar.text ="Curso Guardado"
                        break
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Error al leer los datos de Firebase", error.toException())
            }
        })


        binding.btnGuardar.setOnClickListener{


            val titulo = intent.getStringExtra("titulo")
            val descripcion = intent.getStringExtra("descripcion")
            val imagenUrl = intent.getStringExtra("imagenUrl")
            val enlace = intent.getStringExtra("enlace")
            val valoracion = intent.getDoubleExtra("valoracion",0.0)
            val duracion = intent.getIntExtra("duracion",0)
            val idioma = intent.getStringExtra("idioma")
            val estudiantes = intent.getDoubleExtra("estudiantes",0.0)
            val fondo = intent.getStringExtra("imagenFondo")
            val empresa = intent.getStringExtra("empresa")

            val curso = Cursos(
                titulo = titulo!!,
                descripcion = descripcion!!,
                imagenUrl = imagenUrl!!,
                enlace = enlace!!,
                valoracion = valoracion,
                duracion = duracion,
                idioma = idioma!!,
                estudiantes = estudiantes,
                imagenFondo = fondo!!,
                empresa = empresa!!


            )

            // Guardar el curso en la base de datos
            val currentUser = FirebaseAuth.getInstance().currentUser
            if (currentUser != null) {
                val userId = currentUser.uid
                val cursosGuardadosRef = FirebaseDatabase.getInstance().getReference("usuarios").child(userId).child("cursosGuardados")
                val cursoRef = cursosGuardadosRef.push()
                cursoRef.setValue(curso)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Curso guardado correctamente", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al guardar el curso", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Debe iniciar sesión para guardar el curso", Toast.LENGTH_SHORT).show()
            }
        }






        var adRequest = AdRequest.Builder().build()
        //original
        //ca-app-pub-5486388630970825/8955067655
        //test - ca-app-pub-3940256099942544/5224354917
        RewardedAd.load(this,"ca-app-pub-5486388630970825/8955067655", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                 rewardedAd = null
                binding.enlaceButton.isEnabled = false
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
                binding.CargandoProgress.visibility = View.GONE
                binding.enlaceButton.isEnabled = true
                rewardedAd = ad
            }
        })

         //TODO: agregar sistema de likes y comentarios a los detallesActivity


        // Obtener los datos del curso desde el intent
        val titulo = intent.getStringExtra("titulo")

        val descripcion = intent.getStringExtra("descripcion")
        val imagenUrl = intent.getStringExtra("imagenUrl")
        val enlace = intent.getStringExtra("enlace")
        val valoracion = intent.getDoubleExtra("valoracion",0.0)
        val duracion = intent.getIntExtra("duracion",0)
        val idioma = intent.getStringExtra("idioma")
        val estudiantes = intent.getDoubleExtra("estudiantes",0.0)


        // Mostrar los datos del curso en la vista
        binding.tituloCurso.text = titulo
        binding.ratingBar.rating = valoracion.toFloat()
        binding.txtDuracion.text = "$duracion horas"
        binding.txtIdioma.text = idioma
        binding.txtEstudiantes.text = estudiantes.toString()

        binding.descripcionCurso.text = descripcion
        Glide.with(this).load(imagenUrl).into(binding.imagenCurso)

        rewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad dismissed fullscreen content.")
                rewardedAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.e(TAG, "Ad failed to show fullscreen content.")
                rewardedAd = null
            }

            override fun onAdImpression() {
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }

        // Configurar el botón para abrir el enlace en un WebView
        binding.enlaceButton.setOnClickListener {
            val intent = Intent(this, WebViewCurso::class.java)
            intent.putExtra("enlace", enlace)
            intent.putExtra("titulo", titulo)
            startActivity(intent)

            rewardedAd?.let { ad ->
                ad.show(this, OnUserEarnedRewardListener { rewardItem ->
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    Log.d(TAG, "User earned the reward.")
                 //   Toast.makeText(this@CursoDetallesActivity,"CHIDO",Toast.LENGTH_SHORT).show()
                })
            } ?: run {
                Log.d(TAG, "The rewarded ad wasn't ready yet.")
               // Toast.makeText(this@CursoDetallesActivity,"nooooo",Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun addToFavorites(curso: Cursos) {
        val user = mAuth?.currentUser
        val userId = user?.uid ?: return // Si el usuario no está autenticado, no se hace nada

        // Obtener una referencia al nodo "Favoritos" del usuario actual
        val favoritosRef = FirebaseDatabase.getInstance().getReference("usuarios/$userId/Favoritos")

        // Crear un mapa con los datos del curso
        val cursoData = hashMapOf(
            "titulo" to curso.titulo,
            "descripcion" to curso.descripcion,
            "imagenUrl" to curso.imagenUrl,
            "enlace" to curso.enlace,
            "categoria" to curso.categoria,
            "empresa" to curso.empresa,
            "imagenFondo" to curso.imagenFondo,
            "valoracion" to curso.valoracion,
            "duracion" to curso.duracion,
            "idioma" to curso.idioma,
            "estudiantes" to curso.estudiantes,
            "certificado" to curso.certificado
        )

        // Generar una clave única para el nodo del curso en la base de datos
        val cursoRef = favoritosRef.push()
        val cursoId = cursoRef.key

        // Guardar los datos del curso en la base de datos
        cursoId?.let {
            cursoRef.setValue(cursoData).addOnSuccessListener {
                Toast.makeText(this, "Curso agregado a favoritos", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "Error al agregar curso a favoritos", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun eliminarFav(){
        Toast.makeText(this,"Curso quitado de fav",Toast.LENGTH_SHORT).show()
        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        ref.child(mAuth?.uid!!).child("Favoritos").child(cursoID)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(this,"Curso removido",Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener {
                Toast.makeText(this,"Curso fallado al remover xd",Toast.LENGTH_SHORT).show()

            }
    }


    private fun checkIsFavorite(){
        val ref = FirebaseDatabase.getInstance().getReference("usuarios")
        ref.child(mAuth?.uid!!).child("Favoritos").child(cursoID)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    isFav = snapshot.exists()
                    if(isFav){
//si esta en favoritos
                        binding.btnGuardar.text ="Remove Favoritos"
                    }else{
                        binding.btnGuardar.text ="Agregar Favoritos"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()

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
