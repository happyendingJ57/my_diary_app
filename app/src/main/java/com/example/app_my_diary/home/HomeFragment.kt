package com.example.app_my_diary.home

import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BasePermissionFragment
import com.example.app_my_diary.databinding.FragmentHomeBinding
import com.example.app_my_diary.utils.SystemUtils

class HomeFragment : BasePermissionFragment<FragmentHomeBinding>() {
    override fun initView() {
        binding?.startDiary?.setOnClickListener {
            if (!SystemUtils.hasPermissions(
                    requireContext(), *SystemUtils.getStoragePermissions()
                )
            ) {
                storageResultLauncher.launch(SystemUtils.getStoragePermissions())
            } else {
                findNavController().navigate(R.id.nav_love_diary)
            }
        }
        binding?.textViewSetting?.setOnClickListener {
            findNavController().navigate(R.id.settingFragment)
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initViewModel() {

    }

    override fun onPermissionResult() {
        if (SystemUtils.hasPermissions(requireContext(), *SystemUtils.getStoragePermissions())) {
            findNavController().navigate(R.id.nav_love_diary)
        } else {
            SystemUtils.showAlertPermissionNotGrant(binding!!, requireActivity())
        }
    }
}