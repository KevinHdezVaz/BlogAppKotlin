package com.kevin.blogappkotlin.ui.auth

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kevin.blogappkotlin.R

import com.kevin.blogappkotlin.core.Result
import com.kevin.blogappkotlin.data.remote.auth.AuthDataSource
import com.kevin.blogappkotlin.databinding.FragmentRegisterBinding
import com.kevin.blogappkotlin.domain.auth.AuthRepoImplements
import com.kevin.blogappkotlin.presentation.auth.AuthViewModel
import com.kevin.blogappkotlin.presentation.auth.LoginScreeViewModelFactory
import com.kevin.blogappkotlin.utils.validateRegisterSignUp.Companion.validateEmail
import com.kevin.blogappkotlin.utils.validateRegisterSignUp.Companion.validateForm
import com.kevin.blogappkotlin.utils.validateRegisterSignUp.Companion.validatePassword


class RegisterFragment : Fragment(R.layout.fragment_register) {
    var simpleVideoView: VideoView? = null
    var mediaControls: MediaController? = null
    private lateinit var binding : FragmentRegisterBinding
    private val viewmodel by viewModels<AuthViewModel> {
        LoginScreeViewModelFactory(AuthRepoImplements(AuthDataSource()))
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentRegisterBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        getSignUp()



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

     fun getSignUp(){


        binding.btnSignUp.setOnClickListener {
            val username = binding.editTextUser.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmpassword = binding.editTextConfirmPassword.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()


            if(password != confirmpassword){
                binding.editTextConfirmPassword.error = "Password does not match"
                binding.editTextPassword.error = "Password does not match"
                return@setOnClickListener //retorna para digitar
            }

            if(!validateEmail(email)) binding.editTextEmail.error ="This is not valid email"
            if(!validatePassword(password)) binding.editTextPassword.error="You Better your password"
            if(validateForm(email,password, passwordConfirmation = confirmpassword))
                Toast.makeText(requireContext(),"Done!",Toast.LENGTH_SHORT).show()



            if(username.isEmpty()) binding.editTextUser.error="Username not incluide"



        createuser(email,password,username)



        }

    }

    private fun createuser(email: String, password: String, username: String) {
        viewmodel.SignUp(email,password,username).observe(viewLifecycleOwner, Observer {

                when(it){
                    is  Result.Loading ->{

                        binding.progresBar.visibility = View.VISIBLE
                        binding.btnSignUp.isEnabled= false
                    }
                    is  Result.Success ->{
                        binding.progresBar.visibility = View.GONE
                        findNavController().navigate(R.id.action_registerFragment_to_setupProfileFragment)


                    }
                    is  Result.Failure ->{
                        binding.progresBar.visibility = View.GONE
                        Toast.makeText(requireContext(),"Ocurrio un error ${it.exception}",Toast.LENGTH_SHORT).show()
                        binding.btnSignUp.isEnabled= true
                    }

            }
        })
    }
}