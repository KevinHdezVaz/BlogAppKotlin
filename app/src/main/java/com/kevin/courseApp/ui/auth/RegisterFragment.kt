package com.kevin.courseApp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.kevin.courseApp.R

import com.kevin.courseApp.core.Result
import com.kevin.courseApp.data.remote.auth.AuthDataSource
import com.kevin.courseApp.databinding.FragmentRegisterBinding
import com.kevin.courseApp.domain.auth.AuthRepoImplements
import com.kevin.courseApp.presentation.auth.AuthViewModel
import com.kevin.courseApp.presentation.auth.LoginScreeViewModelFactory
import com.kevin.courseApp.ui.main.HomeFragment
import com.kevin.courseApp.utils.animacionProgress
import com.kevin.courseApp.utils.animacionProgress.Companion.esconderCarga
import com.kevin.courseApp.utils.animacionProgress.Companion.mostrarCarga
import com.kevin.courseApp.utils.validateRegisterSignUp.Companion.validateEmail
import com.kevin.courseApp.utils.validateRegisterSignUp.Companion.validateForm
import com.kevin.courseApp.utils.validateRegisterSignUp.Companion.validatePassword


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding : FragmentRegisterBinding
    private val viewmodel by viewModels<AuthViewModel> {
        LoginScreeViewModelFactory(AuthRepoImplements(AuthDataSource()))
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentRegisterBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        getSignUp()



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




            if(username.isEmpty()) binding.editTextUser.error="Username not incluide"



        createuser(email,password,username)



        }

    }

    private fun createuser(email: String, password: String, username: String) {
        viewmodel.SignUp(email,password,username).observe(viewLifecycleOwner, Observer {

                when(it){
                    is  Result.Loading ->{

                        mostrarCarga(requireContext())
                        binding.btnSignUp.isEnabled= false
                    }
                    is  Result.Success ->{
                        esconderCarga()
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)


                    }
                    is  Result.Failure ->{
                        mostrarCarga(requireContext())
                        Toast.makeText(requireContext(),"Ocurrio un error ${it.exception}",Toast.LENGTH_SHORT).show()
                        binding.btnSignUp.isEnabled= true
                    }

            }
        })
    }
}