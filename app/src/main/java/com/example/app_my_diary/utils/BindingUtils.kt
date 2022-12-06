package com.example.app_my_diary.utils

import android.os.SystemClock
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

fun Toolbar.setSafeMenuClickListener(onSafeClick: (MenuItem?) -> Unit) {
    val safeMenuClickListener = SafeMenuClickListener(defaultInterval = 500, onSafeClick = ({
        onSafeClick(it)
    }))
    setOnMenuItemClickListener(safeMenuClickListener)
}

fun View.clickWithDebounce(debounceTime: Long = 600L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()
            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

@BindingAdapter("android:bindStoryDetail")
fun ImageView.bindStoryDetail(uri: String) {
    Glide.with(this).load(uri).into(this)
}

@BindingAdapter("android:setDiaryTime")
fun TextView.setDiaryTime(time : Long){
    text = SystemUtils.getDiaryTime(time)
}