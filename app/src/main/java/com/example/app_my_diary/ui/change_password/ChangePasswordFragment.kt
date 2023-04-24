package com.example.app_my_diary.ui.change_password

import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentChangePasswordBinding
import com.example.app_my_diary.utils.snackbar.SnackBarType
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordFragment : BaseViewModelFragment<FragmentChangePasswordBinding>() {
    override fun initViewModel() {

    }

    override fun initView() {
        binding?.textViewChangePassword?.setOnClickListener {
            changePassword()
        }
    }

    override fun getLayout(): Int = R.layout.fragment_change_password

    private fun changePassword() {
        val currentPassword = binding?.etCurrentPassword?.text.toString().trim()
        val newPassword = binding?.etNewPassword?.text.toString().trim()
        val comfirmPassword = binding?.etConfirmPassword?.text.toString().trim()
        if (currentPassword.isNotBlank() && newPassword.isNotBlank() && comfirmPassword.isNotBlank()) {
            if (newPassword == comfirmPassword) {
                val auth = FirebaseAuth.getInstance()
                val user = auth.currentUser
                if (user != null && user.email != null){
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, currentPassword)

                    user.reauthenticate(credential)
                        .addOnCompleteListener {
                           if (it.isSuccessful){
                               user!!.updatePassword(newPassword)
                                   .addOnCompleteListener { task ->
                                       if (task.isSuccessful) {
                                           mainActivity.showSnackBar(SnackBarType.Success, resources.getString(R.string.title_success), "change password Success")
                                       }
                                   }
                           }else{
                               mainActivity.showSnackBar(
                                   SnackBarType.Success,
                                   resources.getString(R.string.title_success),
                                   "failed"
                               )
                           }
                        }
                }else{

                }
            } else {
                mainActivity.showSnackBar(
                    SnackBarType.Warning,
                    resources.getString(R.string.title_warning),
                    "password fald"
                )
            }
        } else {
            mainActivity.showSnackBar(
                SnackBarType.Warning,
                resources.getString(R.string.title_warning),
                "Enter all fildes"
            )
        }
    }
}