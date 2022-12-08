package com.example.app_my_diary.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.example.app_my_diary.R

abstract class BaseViewModelDialogFragment<V : ViewDataBinding> : BaseFullAlertDialog<V>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_FullScreen)
        initViewModel()
    }

    abstract  fun initViewModel()
}