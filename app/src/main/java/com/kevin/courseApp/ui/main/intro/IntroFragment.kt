package com.kevin.courseApp.ui.main.intro

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.icu.text.Transliterator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.kevin.courseApp.R
import com.kevin.courseApp.databinding.FragmentIntroBinding
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

class IntroFragment: Fragment() {

    private lateinit var mViewPager: ViewPager2
    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroBinding.inflate(inflater, container, false)
        val view = binding.root


        if (!isFirstTime(requireContext())) {
            findNavController().navigate(R.id.action_introFragment2_to_homeFragment)
        }

         binding.btnCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment2_to_homeFragment)
        }

        // Encontrar la vista del ViewPager en tu layout y configurarla
        mViewPager = view.findViewById(R.id.viewPager)
        mViewPager.adapter = adapter(requireActivity(), requireContext())
        TabLayoutMediator(view.findViewById(R.id.pageIndicator), mViewPager) { _, _ -> }.attach()
        mViewPager.offscreenPageLimit = 1




        return view
    }


    private fun isFirstTime(context: Context): Boolean {
        val preferences = context.getSharedPreferences("myPreferencesFile",  MODE_PRIVATE)
        val ranBefore = preferences.getBoolean("RanBefore4", false)
        if (!ranBefore) {
            // first time
            val editor = preferences.edit()
            editor.putBoolean("RanBefore4", true)
            editor.apply()
        }
        return !ranBefore
    }



}
