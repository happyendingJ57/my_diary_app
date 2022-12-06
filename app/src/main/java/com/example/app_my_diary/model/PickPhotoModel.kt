package com.example.app_my_diary.model


data class PickPhotoModel(
    val photoModelList: MutableList<PhotoModel>,
    val pickPhotoType: PickPhotoType
)
