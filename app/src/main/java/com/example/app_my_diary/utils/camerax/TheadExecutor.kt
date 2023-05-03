package com.example.app_my_diary.utils.camerax

import android.os.Handler
import java.util.concurrent.Executor

open class TheadExecutor(protected val handler: Handler) : Executor {
    override fun execute(runnable: Runnable) {
        handler.post(runnable)
    }
}