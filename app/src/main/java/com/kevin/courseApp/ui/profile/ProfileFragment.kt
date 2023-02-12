package com.kevin.courseApp.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kevin.courseApp.R
import com.kevin.courseApp.databinding.FragmentProfileBinding


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