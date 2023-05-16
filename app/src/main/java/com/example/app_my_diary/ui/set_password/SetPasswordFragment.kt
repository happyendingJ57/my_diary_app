package com.example.app_my_diary.ui.set_password

import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.AbsBaseFragment
import com.example.app_my_diary.databinding.FragmentSetPasswordBinding
import com.example.app_my_diary.utils.snackbar.SnackBarType

class SetPasswordFragment : AbsBaseFragment<FragmentSetPasswordBinding>() {
    override fun initView() {
        binding?.textViewSetPassword?.setOnClickListener {
            setPassWord()
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_set_password
    }

    private fun setPassWord() {
        val newPassword = binding?.etNewPassword?.text.toString()
        val confirmPassword = binding?.etConfirmPassword?.text.toString()
        if (newPassword.isBlank() || confirmPassword.isBlank()) {
            mainActivity.showSnackBar(
                SnackBarType.Warning,
                resources.getString(R.string.title_warning),
                "Vui lòng không để trống thông tin"
            )
        } else {
            if (newPassword == confirmPassword) {
                dataPref.putStringValue("mat_khau_moi", newPassword)
                dataPref.putBooleanValue("da_co_mat_khau", true)
                findNavController().navigate(R.id.homeFragment)
            } else {
                mainActivity.showSnackBar(
                    SnackBarType.Warning,
                    resources.getString(R.string.title_warning),
                    "Thông tin nhập chưa giống nhau"
                )
            }
        }
    }
}