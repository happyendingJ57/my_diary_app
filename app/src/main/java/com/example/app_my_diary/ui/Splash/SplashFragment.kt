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
            if (dataPref.getBooleanValue("da_co_mat_khau")){
                findNavController().navigate(R.id.secondPasswordFragment)
            }else{
                findNavController().navigate(R.id.setPasswordFragment)
            }
        }, 1000)
    }

    override fun getLayout(): Int = R.layout.fragment_splash
}