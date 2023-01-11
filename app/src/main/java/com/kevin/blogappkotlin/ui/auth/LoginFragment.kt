package com.kevin.blogappkotlin.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.kevin.blogappkotlin.R
import com.kevin.blogappkotlin.core.Resource
import com.kevin.blogappkotlin.data.remote.auth.LoginDataSource
import com.kevin.blogappkotlin.databinding.FragmentLoginBinding
import com.kevin.blogappkotlin.domain.auth.LoginRepoImplements
import com.kevin.blogappkotlin.presentation.auth.LoginScreeViewModel
import com.kevin.blogappkotlin.presentation.auth.LoginScreeViewModelFactory

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val viewmodel by viewModels<LoginScreeViewModel> {
        LoginScreeViewModelFactory(LoginRepoImplements(LoginDataSource()))
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

    private fun signIn(email: String,password: String){

        viewmodel.SignIn(email,password).observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading ->{
                    binding.progresBar.visibility = View.VISIBLE
                    binding.btnSignin.isEnabled = false
                }

                is Resource.Success->{
                    binding.progresBar.visibility = View.GONE
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)

                }

                is Resource.Failure ->{
                    binding.progresBar.visibility = View.GONE
                    binding.btnSignin.isEnabled = true
                    Toast.makeText(requireContext(), "Error ${it.exception}", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

}