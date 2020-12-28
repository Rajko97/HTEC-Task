package com.htec.task

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class HtecApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}