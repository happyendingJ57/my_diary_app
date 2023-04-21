package com.example.app_my_diary.ui.sign_up

import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentSignupBinding

class SignUpFragment : BaseViewModelFragment<FragmentSignupBinding>() {
    override fun initViewModel() {
    }

    override fun initView() {
        binding?.textViewLogin?.setOnClickListener {
            findNavController().navigate(R.id.loginFragment  )
        }
    }

    override fun getLayout(): Int = R.layout.fragment_signup
}