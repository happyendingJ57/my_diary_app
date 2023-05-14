package com.example.app_my_diary.ui.profile

import com.example.app_my_diary.R
import com.example.app_my_diary.base.AbsBaseFragment
import com.example.app_my_diary.databinding.FragmentProfileBinding
import com.example.app_my_diary.model.UserModel

class ProfileFragment : AbsBaseFragment<FragmentProfileBinding>() {

    override fun initView() {
        binding?.tvSaveInformation?.setOnClickListener {
            saveUser()
        }
        setUser()
    }

    override fun getLayout(): Int = R.layout.fragment_profile

    private fun saveUser() {
        val firstName = binding?.etFirstName?.text.toString()
        val lastName = binding?.etLastName?.text.toString()
        val yearOfBirth = binding?.etYearOfBirth?.text.toString()
        val gender = binding?.etGender?.text.toString()
        val gmail = binding?.etEmailUser?.text.toString()

        val userModel = UserModel(
            firstName = firstName,
            lastName = lastName,
            yearOfBirthday = yearOfBirth,
            gender = gender,
            gmail = gmail
        )
        dataPref.setUserModel(userModel)
    }

    private fun setUser() {
        val userModel = dataPref.getUserModel()
        binding?.etFirstName?.setText(userModel.firstName.toString())
        binding?.etLastName?.setText(userModel.lastName.toString())
        binding?.etYearOfBirth?.setText(userModel.lastName.toString())
        binding?.etEmailUser?.setText(userModel.gmail.toString())
        binding?.etGender?.setText(userModel.gender.toString())
    }
}