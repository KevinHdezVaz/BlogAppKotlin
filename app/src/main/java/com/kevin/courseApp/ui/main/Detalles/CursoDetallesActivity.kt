package com.kevin.courseApp.ui.main.Detalles

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.kevin.courseApp.databinding.ActivityCursoDetallesBinding


class CursoDetallesActivity : AppCompatActivity() {
    private var rewardedAd: RewardedAd? = null
    private final var TAG = "MainActivity"

    private lateinit var binding: ActivityCursoDetallesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCursoDetallesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.enlaceButton.isEnabled = false


        var adRequest = AdRequest.Builder().build()
        RewardedAd.load(this,"ca-app-pub-5486388630970825/8955067655", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                 rewardedAd = null
                binding.enlaceButton.isEnabled = false
            }

            override fun onAdLoaded(ad: RewardedAd) {
                Log.d(TAG, "Ad was loaded.")
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

        // Mostrar los datos del curso en la vista
        binding.tituloCurso.text = titulo
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
                    Toast.makeText(this@CursoDetallesActivity,"CHIDO",Toast.LENGTH_SHORT).show()
                })
            } ?: run {
                Log.d(TAG, "The rewarded ad wasn't ready yet.")
                Toast.makeText(this@CursoDetallesActivity,"nooooo",Toast.LENGTH_SHORT).show()
            }
        }
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
