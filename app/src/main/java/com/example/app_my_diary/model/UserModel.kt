package com.example.app_my_diary.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UserModel(
    val firstName: String = "",
    val lastName: String = "",
    val yearOfBirthday: String = "",
    val gender: String = "",
    val gmail: String = ""
) : Parcelable