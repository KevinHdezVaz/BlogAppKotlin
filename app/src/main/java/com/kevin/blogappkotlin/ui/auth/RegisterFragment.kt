package com.kevin.blogappkotlin.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
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
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)


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