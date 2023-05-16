package com.example.app_my_diary.ui.setting

import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentSetttingBinding

class SettingFragment : BaseViewModelFragment<FragmentSetttingBinding>() {
    override fun initViewModel() {
    }

    override fun initView() {
        binding?.textViewChangeSecondPassword?.setOnClickListener {
            findNavController().navigate(R.id.changePasswordFragment)
        }
        binding?.tvThongTinCaNhan?.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigateUp()
        }
    }

    override fun getLayout(): Int = R.layout.fragment_settting
}