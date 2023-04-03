package com.kevin.courseApp.ui.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kevin.courseApp.R
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.core.hideKeyboard
import com.kevin.courseApp.data.remote.auth.AuthDataSource
import com.kevin.courseApp.databinding.FragmentLoginBinding
import com.kevin.courseApp.domain.auth.AuthRepoImplements
import com.kevin.courseApp.presentation.auth.AuthViewModel
import com.kevin.courseApp.presentation.auth.LoginScreeViewModelFactory
import com.kevin.courseApp.ui.main.HomeFragment
import com.kevin.courseApp.utils.animacionProgress.Companion.esconderCarga
import com.kevin.courseApp.utils.animacionProgress.Companion.mostrarCarga


class LoginFragment : Fragment(R.layout.fragment_login) {
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

        binding.textoaRegistro.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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
            validateCredentials(requireContext(),email,password)
            signIn(email,password)
        }
    }

    private fun validateCredentials(context: Context, email:String, password:String){
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

        viewmodel.SignIn( email,password).observe(viewLifecycleOwner, Observer {
            when(it){
                is  Result.Loading ->{
                 mostrarCarga(requireContext())
                    binding.btnSignin.isEnabled = false
                }

                is  Result.Success->{

                      esconderCarga()

                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)


                }

                is  Result.Failure ->{
                    esconderCarga()

                    binding.btnSignin.isEnabled = true
                    Toast.makeText(requireContext(), "Los datos no coinciden, crea una cuenta.", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

}