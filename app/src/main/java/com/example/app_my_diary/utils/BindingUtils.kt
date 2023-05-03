package com.example.app_my_diary.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.SystemClock
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.app_my_diary.R
import com.example.app_my_diary.diary.calendar.model.Date
import com.example.app_my_diary.model.ActionModel
import com.example.app_my_diary.model.EventActionModel
import com.example.app_my_diary.model.PhotoModel
import com.example.app_my_diary.ui.event.EventModel
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
@SuppressLint("SetTextI18n")
@BindingAdapter("android:setMyEventCount")
fun TextView.setMyEventCount(eventModel: EventModel) {
    when (eventModel.calType) {
        1 -> {
            text = if (eventModel.date < System.currentTimeMillis()) {
                resources.getString(R.string.action_done)
            } else {
                val daysLeft = SystemUtils.calMyEventDayLeft(eventModel.date) + 1
                if (daysLeft / 365L == 0L) {
                    "$daysLeft Days Left"
                } else {
                    if (daysLeft % 365 == 0L) {
                        "${daysLeft / 365} Years Left"
                    } else {
                        "${daysLeft / 365} Years ${(daysLeft % 365)} Days Left"
                    }
                }
            }
        }
        2 -> {
            val daysAgo = SystemUtils.calMyEventDayLeft(eventModel.date)
            text = if (daysAgo / 365L == 0L) {
                "$daysAgo Days Ago"
            } else {
                if (daysAgo % 365 == 0L) {
                    "${daysAgo / 365} Years Ago"
                } else {
                    "${daysAgo / 365} Years ${(daysAgo % 365)} Days Ago"
                }
            }
        }
        3 -> {
            val cal = SystemUtils.calAnnuallyType(eventModel.date)
            text = if (cal == -1L) {
                "Today"
            } else {
                "${cal + 1} Days Left"
            }
        }
    }
}

@BindingAdapter("android:noResultSrc")
fun ImageView.noResultSrc(isCommon: Boolean) {
    if (isCommon) {
        setImageResource(R.drawable.ic_img_no_internet)
    } else {
        setImageResource(R.drawable.ic_empty_my_event)
    }
}
@BindingAdapter("android:setMyEventDate")
fun TextView.setMyEventDate(eventModel: EventModel) {
    text = SystemUtils.getMyEventTime(eventModel)
}
@BindingAdapter("android:bindEventThumb")
fun ShapeableImageView.bindEventThumb(thumbPath: String) {
    if (thumbPath.isNotEmpty()) {
        Glide.with(this).load(thumbPath)
            .placeholder(R.drawable.ic_default_image)
            .error(R.drawable.ic_default_image).into(this)
    } else {
        setImageResource(R.drawable.banner_default)
    }
}

@BindingAdapter("android:setDetailStoryTime")
fun TextView.setDetailStoryTime(time: Long) {
    text = SystemUtils.getStoryDetailTime(time)
}

@BindingAdapter("android:bindEventCover")
fun ImageView.bindEventCover(thumbPath: String) {
    if (thumbPath.isNotEmpty()) {
        Glide.with(this)
            .load(thumbPath)
            .placeholder(R.drawable.ic_loading_event2)
            .error(R.drawable.ic_loading_event2)
            .into(this)
    } else {
        setImageResource(R.drawable.banner_default)
    }
}
@BindingAdapter("android:setEventTime")
fun TextView.setEventTime(time: Long) {
    text = SystemUtils.getEventTime(time)
}
@BindingAdapter("android:setDayOfWeekstEventTime")
fun TextView.setDayOfWeekstEventTime(time: Long) {
    text = SystemUtils.getDayOfWeeksEventTime(time)
}

@BindingAdapter("android:setMonthEventTime")
fun TextView.setMonthEventTime(time: Long) {
    text = SystemUtils.getMonthEventTime(time)
}
@BindingAdapter("android:setupDateText")
fun TextView.setupDateText(date: Date) {
    setTextColor(
        if (date.isSelected) {
            Color.WHITE
        } else {
            if (date.isNowDate) {
                Color.parseColor("#8295DB")
            } else {
                if(date.isSelfMonthDate){
                    Color.parseColor("#222B45")
                } else {
                    Color.parseColor("#8F9BB3")
                }
            }
        }
    )

    text = "${date.date}"
}

@BindingAdapter("android:setupDateView")
fun View.setupDateView(date: Date) {
    visibility = if (date.hasDiary) {
        setBackgroundResource(if (date.isSelected) R.drawable.bg_date_dot_selected else R.drawable.bg_date_dot_normal)
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

@BindingAdapter("android:setupDateBackground")
fun ConstraintLayout.setupBackground(date: Date) {
    setBackgroundResource(
        if (date.isSelected) {
            R.drawable.bg_date_selected
        } else {
            if (date.isNowDate) {
                R.drawable.bg_date_current_date
            } else {
                R.drawable.bg_date_normal
            }
        }
    )
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