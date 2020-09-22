package com.battisq.news.ui.list_news.recycler

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

class MainThreadExecutor : Executor {

    private var mHandler: Handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable?) {
        if (command != null) {
            mHandler.post(command)
        }
    }
}