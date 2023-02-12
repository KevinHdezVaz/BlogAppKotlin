package com.kevin.courseApp.ui.main

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kevin.courseApp.R
import com.kevin.courseApp.core.Result
import com.kevin.courseApp.core.hide
import com.kevin.courseApp.core.show
import com.kevin.courseApp.data.model.Posts
import com.kevin.courseApp.data.remote.home.HomeScreenDataSource
import com.kevin.courseApp.databinding.FragmentHomeBinding
import com.kevin.courseApp.domain.home.HomeScreenRepoImplement
import com.kevin.courseApp.presentation.HomeScreenViewModel
import com.kevin.courseApp.presentation.HomeScreenViewModelFactory
import com.kevin.courseApp.ui.main.adapter.HomeScreenAdapter
import com.kevin.courseApp.ui.main.adapter.onPostClickListener

class HomeFragment : Fragment(R.layout.fragment_home), onPostClickListener {
    private lateinit var binding: FragmentHomeBinding
    lateinit var epicdialog: Dialog
    private val viewmodel by viewModels<HomeScreenViewModel>
    {
    (HomeScreenViewModelFactory(
            HomeScreenRepoImplement(HomeScreenDataSource())
        ))
    }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            super.onViewCreated(view, savedInstanceState)
            binding = FragmentHomeBinding.bind(view)

            //permisos camara


            viewmodel.fetchLatestPosts().observe(viewLifecycleOwner, Observer {

                when(it){
                    is  Result.Loading ->{

                        mostrarAnimacion()


                     }
                    is  Result.Success ->{
                         
                        binding.rvHome.adapter  = HomeScreenAdapter(it.data,this)


                        esconderAnimacion()

                    }
                    is  Result.Failure ->{

                        esconderAnimacion()

                        Toast.makeText(requireContext(),"Ocurrio un error ${it.exception}",Toast.LENGTH_SHORT).show()

                    }
                }
            })
            //    binding.rvHome.adapter = HomeScreenAdapter(postlist = postlist)

    }

    private fun esconderAnimacion() {


        epicdialog.dismiss()
    }

    private fun mostrarAnimacion() {


        epicdialog = Dialog(requireContext())
        epicdialog.setContentView(R.layout.progress_layout)
        epicdialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        epicdialog.setCanceledOnTouchOutside(false)
        epicdialog.show()

    }


    override fun onlikeBtnClick(post: Posts, liked: Boolean) {
        super.onlikeBtnClick(post, liked)

        viewmodel.registerLikeButtonState(post.id, liked).observe(viewLifecycleOwner){
            when(it){
                is  Result.Loading ->{
                    Toast.makeText(requireContext(),"PROGRESO",Toast.LENGTH_SHORT).show()

                }
                is  Result.Success ->{

                    Toast.makeText(requireContext(),"CHIDO",Toast.LENGTH_SHORT).show()


                }
                is  Result.Failure ->{

                    Toast.makeText(requireContext(),"Ocurrio un error ${it.exception}",Toast.LENGTH_SHORT).show()

                }
            }
        }

    }



}
