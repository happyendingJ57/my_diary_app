package com.example.app_my_diary.ui.Splash

import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentSplashBinding

class SplashFragment : BaseViewModelFragment<FragmentSplashBinding>() {
    override fun initViewModel() {

    }

    override fun initView() {
        binding?.tvTitle?.postDelayed({
            findNavController().navigate(R.id.secondPasswordFragment)
        }, 1000)
    }

    override fun getLayout(): Int = R.layout.fragment_splash
}