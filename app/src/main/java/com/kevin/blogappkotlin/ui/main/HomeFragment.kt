package com.kevin.blogappkotlin.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kevin.blogappkotlin.R
import com.kevin.blogappkotlin.core.Resource
import com.kevin.blogappkotlin.data.remote.HomeScreenDataSource
import com.kevin.blogappkotlin.databinding.FragmentHomeBinding
import com.kevin.blogappkotlin.domain.HomeScreenRepoImplement
import com.kevin.blogappkotlin.presentation.HomeScreenViewModel
import com.kevin.blogappkotlin.presentation.HomeScreenViewModelFactory
import com.kevin.blogappkotlin.ui.main.adapter.HomeScreenAdapter

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val viewmodel by viewModels<HomeScreenViewModel>
    {
    (HomeScreenViewModelFactory(
            HomeScreenRepoImplement(HomeScreenDataSource())
        ))
    }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            super.onViewCreated(view, savedInstanceState)
            binding = FragmentHomeBinding.bind(view)

            viewmodel.fetchLatestPosts().observe(viewLifecycleOwner, Observer {

                when(it){
                    is Resource.Loading ->{

                        binding.progresBar.visibility = View.VISIBLE
                     }
                    is Resource.Success ->{
                        binding.progresBar.visibility = View.GONE
                        binding.rvHome.adapter  = HomeScreenAdapter(it.data)

                    }
                    is Resource.Failure ->{
                        binding.progresBar.visibility = View.GONE
                        Toast.makeText(requireContext(),"Ocurrio un error ${it.exception}",Toast.LENGTH_SHORT).show()

                    }
                }
            })
            //    binding.rvHome.adapter = HomeScreenAdapter(postlist = postlist)

    }
}
