package com.example.app_my_diary.ui.second_password

import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentSecondPasswordBinding
import com.example.app_my_diary.utils.snackbar.SnackBarType

class SecondPasswordFragment : BaseViewModelFragment<FragmentSecondPasswordBinding>() {
    override fun initViewModel() {
    }

    override fun initView() {
        binding?.tvXacNhan?.setOnClickListener {
            confirmPassword()
        }
    }

    override fun getLayout(): Int = R.layout.fragment_second_password

    private fun confirmPassword() {
        val password = binding?.etMaBaoVe?.text.toString()
        if (password.isBlank()) {
            mainActivity.showSnackBar(
                SnackBarType.Warning,
                resources.getString(R.string.title_warning),
                "Vui lòng điền mã bảo vệ"
            )
        } else {
            if (dataPref.getStringValue("mat_khau_moi") == password) {
                findNavController().navigate(R.id.homeFragment)
            } else {
                mainActivity.showSnackBar(
                    SnackBarType.Error,
                    resources.getString(R.string.title_error),
                    "Mã bảo vệ không chính xác"
                )
            }
        }
    }
}