package com.example.app_my_diary.utils.camerax

import android.os.Handler
import android.os.Looper

class MainExecutor: TheadExecutor(Handler(Looper.getMainLooper())) {
    override fun execute(runnable: Runnable) {
        handler.post(runnable)
    }
}