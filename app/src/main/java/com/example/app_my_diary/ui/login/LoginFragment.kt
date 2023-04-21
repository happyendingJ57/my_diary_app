package com.example.app_my_diary.ui.login

import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentLoginBinding

class LoginFragment : BaseViewModelFragment<FragmentLoginBinding>() {
    override fun initViewModel() {

    }

    override fun initView() {
        binding?.textViewSignup?.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }
        binding?.textViewLogin?.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    override fun getLayout(): Int = R.layout.fragment_login

}