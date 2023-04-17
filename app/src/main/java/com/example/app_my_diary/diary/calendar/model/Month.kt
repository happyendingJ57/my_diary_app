package com.example.app_my_diary.diary.calendar.model

data class Month(
    var month: Int = -1, var year: Int = -1, var days: MutableList<Date> = mutableListOf()
)
