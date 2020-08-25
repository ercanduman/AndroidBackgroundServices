package com.enbcreative.androidbackgroundservices

import android.app.Application
import com.enbcreative.androidbackgroundservices.utils.Notifications

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Notifications.createNotificationChannel(this)
    }
}