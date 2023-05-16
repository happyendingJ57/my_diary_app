package com.example.app_my_diary.ui.change_password

import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentChangePasswordBinding
import com.example.app_my_diary.utils.snackbar.SnackBarType

class ChangePasswordFragment : BaseViewModelFragment<FragmentChangePasswordBinding>() {
    override fun initViewModel() {

    }

    override fun initView() {
        binding?.textViewChangePassword?.setOnClickListener {
            changePassword()
        }
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun getLayout(): Int = R.layout.fragment_change_password

    private fun changePassword() {
        val currentPassword = binding?.etCurrentPassword?.text.toString()
        val newPassword = binding?.etNewPassword?.text.toString()
        val confirmPassword = binding?.etConfirmPassword?.text.toString()

        if (currentPassword.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
            mainActivity.showSnackBar(
                SnackBarType.Warning,
                resources.getString(R.string.title_warning),
                "Không được để trống thông tin"
            )
        } else {
            if (dataPref.getStringValue("mat_khau_moi") == currentPassword) {
                if (newPassword == confirmPassword) {
                    dataPref.putStringValue("mat_khau_moi", newPassword)
                    findNavController().navigateUp()
                } else {
                    mainActivity.showSnackBar(
                        SnackBarType.Warning,
                        resources.getString(R.string.title_warning),
                        "Mật khẩu mới chưa chính xác"
                    )
                }
            } else {
                mainActivity.showSnackBar(
                    SnackBarType.Warning,
                    resources.getString(R.string.title_warning),
                    "Mật khẩu hiện tại không chính xác"
                )
            }
        }
    }
}