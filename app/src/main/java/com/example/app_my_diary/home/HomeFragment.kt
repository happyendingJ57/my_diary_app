package com.example.app_my_diary.home

import android.widget.Toast
import com.example.app_my_diary.R
import com.example.app_my_diary.databinding.FragmentHomeBinding
import com.hola360.crushlovecalculator.base.basefragment.BaseViewModelFragment

class HomeFragment : BaseViewModelFragment<FragmentHomeBinding>(){
    override fun initView() {
        binding?.startDiary?.setOnClickListener{
            Toast.makeText(context,"ok la",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getLayout(): Int {
       return R.layout.fragment_home
    }

    override fun initViewModel() {

    }
}