package com.enbcreative.androidbackgroundservices.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.enbcreative.androidbackgroundservices.EMPTY_STRING
import com.enbcreative.androidbackgroundservices.ui.MainActivity
import com.enbcreative.androidbackgroundservices.utils.Notifications
import java.lang.Thread.sleep

class ForegroundServiceExample : Service() {
    private var isJobCancelled = false
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
        backgroundWork()

        return START_NOT_STICKY
    }

    /**
     * Start a background work and finish Foreground service when job done.
     */
    private fun backgroundWork() {
        Thread {
            for (i in 1..10) {
                Log.d(TAG, "backgroundWork: run: $i")
                if (isJobCancelled) return@Thread
                sleep(1000)
            }
            Log.d(TAG, "backgroundWork: job completed.")
            stopSelf()
        }.start()
    }

    override fun onDestroy() {
        isJobCancelled = true
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