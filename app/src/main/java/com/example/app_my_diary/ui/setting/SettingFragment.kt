package com.example.app_my_diary.ui.setting

import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentSetttingBinding

class SettingFragment : BaseViewModelFragment<FragmentSetttingBinding>() {
    override fun initViewModel() {
    }

    override fun initView() {
        binding?.textViewChangePassword?.setOnClickListener {
            findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToChangePasswordFragment())
        }
    }

    override fun getLayout(): Int = R.layout.fragment_settting
}