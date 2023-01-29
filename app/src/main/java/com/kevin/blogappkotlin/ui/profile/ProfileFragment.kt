package com.kevin.blogappkotlin.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kevin.blogappkotlin.R
import com.kevin.blogappkotlin.data.remote.auth.AuthDataSource
import com.kevin.blogappkotlin.databinding.FragmentLoginBinding
import com.kevin.blogappkotlin.databinding.FragmentProfileBinding
import com.kevin.blogappkotlin.domain.auth.AuthRepoImplements
import com.kevin.blogappkotlin.presentation.auth.AuthViewModel
import com.kevin.blogappkotlin.presentation.auth.LoginScreeViewModelFactory
import com.kevin.blogappkotlin.ui.auth.LoginFragment


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        val user = FirebaseAuth.getInstance().currentUser
        val auth = FirebaseAuth.getInstance()


        Glide.with(this).load(user?.photoUrl).centerCrop().into(binding.imgProfile)
        binding.txtUser.text = user?.displayName
        Log.d("ASKA","${user?.displayName}")



        binding.btnLogout.setOnClickListener{

            Firebase.auth.signOut()

            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }
}