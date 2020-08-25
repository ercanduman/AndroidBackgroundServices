package com.enbcreative.androidbackgroundservices.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.enbcreative.androidbackgroundservices.EMPTY_STRING
import com.enbcreative.androidbackgroundservices.ui.MainActivity
import com.enbcreative.androidbackgroundservices.utils.Notifications

class ForegroundServiceExample : Service() {
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Service created.")
    }

    /**
     * Codes written here are running on UI thread.
     * All heavy operations should be run on separate threads.
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: Service started.")
        val inputValue = intent?.getStringExtra(MainActivity.KEY_INPUT) ?: EMPTY_STRING

        val notification = Notifications.createNotification(this, inputValue)

        /**
         * If startForeground not called, then android system will kill the service around 1 min.
         */
        startForeground(1, notification)

        /**
         * When work done stopSelf() has to be called which is going to stop service.
         */
        // stopSelf()
        return START_NOT_STICKY
    }


    override fun onDestroy() {
        Log.d(TAG, "onDestroy: Service is destroyed.")
        super.onDestroy()
    }

    companion object {
        private const val TAG = "ForegroundServiceExampl"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}