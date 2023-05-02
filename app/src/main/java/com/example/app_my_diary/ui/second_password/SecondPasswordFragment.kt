package com.example.app_my_diary.ui.second_password

import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentSecondPasswordBinding

class SecondPasswordFragment : BaseViewModelFragment<FragmentSecondPasswordBinding>() {
    override fun initViewModel() {
    }

    override fun initView() {
        binding?.tvXacNhan?.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    override fun getLayout(): Int = R.layout.fragment_second_password
}