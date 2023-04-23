package com.example.app_my_diary.ui.login

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentLoginBinding
import com.example.app_my_diary.utils.snackbar.SnackBarType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : BaseViewModelFragment<FragmentLoginBinding>() {
    override fun initViewModel() {

    }

    override fun initView() {
        binding?.textViewSignUpNow?.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }
        binding?.textViewLogin?.setOnClickListener {
            login()
        }
        binding?.textViewForgotPassword?.setOnClickListener {
            resetPassword()
        }
    }

    override fun getLayout(): Int = R.layout.fragment_login


    private fun login() {
        val auth = FirebaseAuth.getInstance()
        val email = binding?.etEmail?.text.toString().trim()
        val password = binding?.etPassword?.text.toString().trim()

        if (email.isBlank() || password.isEmpty()) {
            mainActivity.showSnackBar(
                SnackBarType.Warning,
                resources.getString(R.string.title_warning),
                "Email or password is empty"
            )
        } else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        findNavController().navigate(R.id.homeFragment)
                    } else {
                        mainActivity.showSnackBar(
                            SnackBarType.Warning,
                            resources.getString(R.string.title_warning),
                            "Login failed"
                        )
                    }
                }
        }
    }

    private fun resetPassword() {
        val auth = FirebaseAuth.getInstance()
        val emailAddress = binding?.etEmail?.text.toString().trim()
        if (emailAddress.isBlank()){
            mainActivity.showSnackBar(
                SnackBarType.Warning,
                resources.getString(R.string.title_warning),
                "Please enter your email"
            )
            return
        }
        auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("TAG", "resetPassword: 1111111")
                mainActivity.showSnackBar(
                    SnackBarType.Success,
                    resources.getString(R.string.title_success),
                    "Please check your email to reset your password"
                )
            }
        }
    }
}