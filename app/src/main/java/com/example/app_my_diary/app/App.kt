package com.example.app_my_diary.app

import android.app.Application
import android.graphics.Point

class App : Application() {
    lateinit var outPoint: Point
    var screenWidthInDp = 360
    var photoColumns = Constants.AT_LEAST_COLUMN
    var storyImageColumns = Constants.AT_LEAST_COLUMN
    override fun onCreate() {
        super.onCreate()
        getScreenDimen()
        photoColumns = SystemUtils.getColumn(this)
        storyImageColumns = SystemUtils.getStoryImageColumn(this)
    }

    private fun getScreenDimen() {
        outPoint = Point(SystemUtils.getScreenWidth(), SystemUtils.getScreenHeight())
        screenWidthInDp = if (SystemUtils.isLandscape(resources)) {
            outPoint.x.coerceAtLeast(outPoint.y)
        } else {
            outPoint.x.coerceAtMost(outPoint.y)
        }
        screenWidthInDp =
            (SystemUtils.convertPixelsToDp(screenWidthInDp.toFloat(), this).toInt()).coerceAtLeast(
                360
            )
    }
}