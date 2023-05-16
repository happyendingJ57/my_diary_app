package com.example.app_my_diary.ui.profile

import androidx.navigation.fragment.findNavController
import com.example.app_my_diary.R
import com.example.app_my_diary.base.AbsBaseFragment
import com.example.app_my_diary.databinding.FragmentProfileBinding
import com.example.app_my_diary.model.UserModel

class ProfileFragment : AbsBaseFragment<FragmentProfileBinding>() {

    override fun initView() {
        binding?.tvSaveInformation?.setOnClickListener {
            saveUser()
            findNavController().navigateUp()
        }
        binding?.toolbar?.setNavigationOnClickListener{
            findNavController().navigateUp()
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
        binding?.etFirstName?.setText(userModel.firstName)
        binding?.etLastName?.setText(userModel.lastName)
        binding?.etYearOfBirth?.setText(userModel.yearOfBirthday)
        binding?.etEmailUser?.setText(userModel.gmail)
        binding?.etGender?.setText(userModel.gender)
    }
}