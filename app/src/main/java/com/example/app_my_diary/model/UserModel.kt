package com.example.app_my_diary.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UserModel(
    val firstName: String? = null,
    val lastName: String? = null,
    val yearOfBirthday: String? = null,
    val gender: String? = null,
    val gmail: String? = null
) : Parcelable