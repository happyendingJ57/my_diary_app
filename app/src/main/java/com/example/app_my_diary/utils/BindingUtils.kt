package com.example.app_my_diary.utils

import android.annotation.SuppressLint
import android.os.SystemClock
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.app_my_diary.EventActionModel
import com.example.app_my_diary.R
import com.example.app_my_diary.model.ActionModel
import com.example.app_my_diary.model.PhotoModel
import com.google.android.material.imageview.ShapeableImageView

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
@BindingAdapter("android:bindDiaryPhoto")
fun ShapeableImageView.bindDiaryPhoto(imageModel: PhotoModel?) {
    if (imageModel != null && !imageModel.isNormalItem) {
        setImageResource(R.drawable.bg_story_add_image)
        scaleType = ImageView.ScaleType.FIT_XY
    } else {
        scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(this).load(imageModel?.uriString)
            .placeholder(R.drawable.ic_default_image)
            .error(R.drawable.ic_default_image).into(this)
    }
}

@BindingAdapter("android:iconForAction")
fun ImageView.iconForAction(actionModel: ActionModel) {
    if (actionModel.selectMode) {
        visibility = View.VISIBLE
        if (actionModel.selected) {
            setImageResource(R.drawable.ic_my_photo_radio_check)
        } else {
            setImageResource(R.drawable.ic_my_photo_radio_button_unchecked)
        }
        setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
    } else {
        if (actionModel.icon == -1) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            setImageResource(actionModel.icon)
        }
        setColorFilter(Utils.fetchPrimaryTextColor(context))
    }

}

@BindingAdapter("android:setStoryTime")
fun TextView.setStoryTime(time: Long) {
    text = SystemUtils.getStoryTime(time)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("android:bindMaximum")
fun TextView.bindMaximum(max: Int) {
    text =
        resources.getString(R.string.pick_photo_story_message) + " $max " + resources.getString(R.string.pick_photo_story_photo)
}

@BindingAdapter("android:bindPickingPhoto")
fun ShapeableImageView.bindPickingPhoto(imageModel: PhotoModel?) {
    if (imageModel != null && !imageModel.isNormalItem) {
        setImageResource(R.drawable.bg_story_add_image)
        scaleType = ImageView.ScaleType.FIT_XY
    } else {
        scaleType = ImageView.ScaleType.CENTER_CROP
        Glide.with(this).load(imageModel?.uriString)
            .placeholder(R.drawable.ic_default_image)
            .error(R.drawable.ic_default_image).into(this)
    }
}

@BindingAdapter("android:setTextTime")
fun TextView.setTextTime(time: Long){
    text = SystemUtils.getTimeOnLy(time)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("android:countPicked")
fun TextView.countPicked(count: Int) {
    text = "$count"
}

@BindingAdapter("android:bindThumbnailFile")
fun ShapeableImageView.bindThumbnailFile(photoModel: PhotoModel?) {
    if (photoModel != null) {
        Glide.with(this).load(photoModel.file)
            .placeholder(R.drawable.ic_default_image)
            .error(R.drawable.ic_default_image).into(this)
    } else {
        Glide.with(this).load(R.drawable.ic_default_image)
            .placeholder(R.drawable.ic_default_image)
            .error(R.drawable.ic_default_image).into(this)
    }
}

@BindingAdapter("android:bindStoryDetail")
fun ImageView.bindStoryDetail(uri: String) {
    Glide.with(this).load(uri).into(this)
}

@BindingAdapter("android:iconForActions")
fun ImageView.iconForActions(eventActionModel: EventActionModel) {
    setImageResource(eventActionModel.icon)
}

@BindingAdapter("android:setDiaryTime")
fun TextView.setDiaryTime(time: Long) {
    text = SystemUtils.getDiaryTime(time)
}