package com.enbcreative.androidbackgroundservices.services

import android.app.IntentService
import android.content.Intent
import android.os.PowerManager
import android.os.SystemClock.sleep
import android.util.Log
import com.enbcreative.androidbackgroundservices.EMPTY_STRING
import com.enbcreative.androidbackgroundservices.ui.MainActivity
import com.enbcreative.androidbackgroundservices.utils.Notifications

class IntentServiceExample : IntentService("IntentServiceExample") {
    init {
        setIntentRedelivery(true) // false means if system kills the service, it wont be created again
    }

    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Intent service created.")
        /**
         * WakeLock should be activated in order to keep CPU running even if screen locked.
         */
        activateWakeLock()
        val notification = Notifications.createNotification(this, "Intent Service running...")
        startForeground(1, notification)
    }

    /**
     * Keeps the CPU running while service is activated,
     * this way service could keep running.
     */
    private fun activateWakeLock() {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ExampleApp:WakeLock")
        wakeLock.acquire(10 * 60 * 1000 /*10 minutes*/)
        Log.d(TAG, "activateWakeLock: WakeLock acquired.")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Intent service destroyed.")
        wakeLock.release()
        Log.d(TAG, "onDestroy: WakeLock released.")
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent: called.")

        val input = intent?.getStringExtra(MainActivity.KEY_INPUT) ?: EMPTY_STRING
        Log.d(TAG, "onHandleIntent: got input as: $input")
        backgroundWork()
    }

    private fun backgroundWork() {
        for (i in 0 until 10) {
            Log.d(TAG, "backgroundWork: run: $i")
            sleep(1000)
        }
    }

    companion object {
        private const val TAG = "IntentServiceExample"
    }
}