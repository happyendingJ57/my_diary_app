package com.example.app_my_diary.ui.login

import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : BaseViewModelFragment<FragmentLoginBinding>() {
    override fun initViewModel() {

    }

    override fun initView() {
        binding?.textViewSignup?.setOnClickListener {
            findNavController().navigate(R.id.signUpFragment)
        }
        binding?.textViewLogin?.setOnClickListener {
            login()
        }
    }

    override fun getLayout(): Int = R.layout.fragment_login


    private fun login() {
        val auth = FirebaseAuth.getInstance()
        val email = binding?.etEmail?.text.toString().trim()
        val password = binding?.etPassword?.text.toString().trim()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "signInWithEmail:success")
//                    val user = auth.currentUser
//                    updateUI(user)
                    findNavController().navigate(R.id.homeFragment)
                } else {
                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "signInWithEmail:failure", task.exception)
//                    Toast.makeText(
//                        baseContext, "Authentication failed.", Toast.LENGTH_SHORT
//                    ).show()
//                    updateUI(null)
                }
            }
    }
}