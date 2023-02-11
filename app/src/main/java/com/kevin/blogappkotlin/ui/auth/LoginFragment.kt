package com.kevin.blogappkotlin.ui.auth

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.kevin.blogappkotlin.R
import com.kevin.blogappkotlin.core.Result
import com.kevin.blogappkotlin.core.hideKeyboard
import com.kevin.blogappkotlin.data.remote.auth.AuthDataSource
import com.kevin.blogappkotlin.databinding.FragmentLoginBinding
import com.kevin.blogappkotlin.domain.auth.AuthRepoImplements
import com.kevin.blogappkotlin.presentation.auth.AuthViewModel
import com.kevin.blogappkotlin.presentation.auth.LoginScreeViewModelFactory

class LoginFragment : Fragment(R.layout.fragment_login) {
    var simpleVideoView: VideoView? = null
    var mediaControls: MediaController? = null
    private lateinit var binding: FragmentLoginBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val viewmodel by viewModels<AuthViewModel> {
        LoginScreeViewModelFactory(AuthRepoImplements(AuthDataSource()))
    }

    //forma de inicializar una propiedad "perezosa"
    /**
     * la propiedad solo se inicializara la primera vz
     * que se acceda a ella y se guardara en memoria
     *
     * */



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentLoginBinding.bind(view)

        super.onViewCreated(view, savedInstanceState)
isUserLoggin()
        duLogin()
        GotosignUp()




        if (mediaControls == null) {
            mediaControls = MediaController(context)
            mediaControls!!.setAnchorView(simpleVideoView)
        }
        binding.vvFondo.setOnPreparedListener { mp ->
            val videoRatio = mp.videoWidth / mp.videoHeight.toFloat()
            val screenRatio: Float =
                binding.vvFondo.width / binding.vvFondo.height.toFloat()
            val scaleX = videoRatio / screenRatio
            if (scaleX >= 1f) {
                binding.vvFondo.scaleX = scaleX
            } else {
                binding.vvFondo.scaleY = 1f / screenRatio
            }
        }

        binding.vvFondo.setVideoURI(Uri.parse("android.resource://" + "com.kevin.blogappkotlin" + "/" + R.raw.videonfoo))
        binding.vvFondo.start()
        binding.vvFondo.setOnCompletionListener { binding.vvFondo.start() }
        binding.vvFondo.setOnErrorListener { _, _, _ ->
            false
        }

    }

    fun isUserLoggin(){
       /**
        * Si el usuario essta logueado (diferente de nulo) pasar al home Fragment
        * */

        firebaseAuth.currentUser?.let {

            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    fun duLogin(){
        binding.btnSignin.setOnClickListener{
            //trim : sirve para que cuando se encuentran espacios adelante o atras, no se
            // tomen en cuenta, solo el texto escrito.

            it.hideKeyboard()

            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            validateCredentials(email,password)
            signIn(email,password)
        }
    }

    private fun validateCredentials(email:String, password:String){
        if(email.isEmpty()){
            binding.editTextEmail.error = "Email esta vacio"
            return
        }
        if(password.isEmpty()){
            binding.editTextPassword.error = "Email esta vacio"
            return
        }
    }
    private fun GotosignUp(){

        binding.txtSignup.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)

        }
     }
    private fun signIn(email: String,password: String){

        viewmodel.SignIn(email,password).observe(viewLifecycleOwner, Observer {
            when(it){
                is  Result.Loading ->{
                    binding.progresBar.visibility = View.VISIBLE
                    binding.btnSignin.isEnabled = false
                }

                is  Result.Success->{
                    binding.progresBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)


                }

                is  Result.Failure ->{
                    binding.progresBar.visibility = View.GONE
                    binding.btnSignin.isEnabled = true
                    Toast.makeText(requireContext(), "Error ${it.exception}", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

}