package com.example.app_my_diary.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding

abstract class BaseViewModelFragment<V : ViewDataBinding> : AbsBaseFragment<V>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    abstract fun initViewModel()
}