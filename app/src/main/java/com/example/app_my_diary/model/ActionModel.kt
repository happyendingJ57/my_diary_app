package com.example.app_my_diary.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActionModel(val icon: Int, val title: String, val selectMode: Boolean,var selected: Boolean = false) : Parcelable
