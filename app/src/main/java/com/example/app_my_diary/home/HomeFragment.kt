package com.example.app_my_diary.home

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentHomeBinding

class HomeFragment : BaseViewModelFragment<FragmentHomeBinding>(){
    override fun initView() {
        binding?.startDiary?.setOnClickListener{
            findNavController().navigate(R.id.nav_love_diary)
        }
    }

    override fun getLayout(): Int {
       return R.layout.fragment_home
    }

    override fun initViewModel() {

    }
}