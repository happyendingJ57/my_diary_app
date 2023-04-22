package com.example.app_my_diary.ui.sign_up

import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.BaseViewModelFragment
import com.example.app_my_diary.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : BaseViewModelFragment<FragmentSignupBinding>() {
    override fun initViewModel() {
    }

    override fun initView() {
        binding?.textViewLogin?.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
        binding?.textViewSignup?.setOnClickListener {
            signUp()
        }
    }

    override fun getLayout(): Int = R.layout.fragment_signup

    private fun signUp() {
        val auth = FirebaseAuth.getInstance()
        val email = binding?.etEmail?.text.toString().trim()
        val password = binding?.etPassword?.text.toString().trim()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success")
//                    val user = auth.currentUser
//                    updateUI(user)
                    findNavController().navigate(R.id.loginFragment)
                } else {
                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                    Toast.makeText(
//                        baseContext, "Authentication failed.", Toast.LENGTH_SHORT
//                    ).show()
//                    updateUI(null)
                    findNavController().navigate(R.id.homeFragment)
                }
            }
    }
}