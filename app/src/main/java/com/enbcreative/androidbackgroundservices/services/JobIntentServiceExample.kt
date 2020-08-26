package com.enbcreative.androidbackgroundservices.services

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.core.app.JobIntentService
import com.enbcreative.androidbackgroundservices.EMPTY_STRING
import com.enbcreative.androidbackgroundservices.ui.MainActivity

class JobIntentServiceExample : JobIntentService() {
    /**
     * Do not need to handle WakeLock as in IntentServiceExample
     * JobIntentService handles the WakeLock automatically
     */
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Job Intent service created.")
    }

    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "onHandleWork: called.")
        val input = intent.getStringExtra(MainActivity.KEY_INPUT) ?: EMPTY_STRING
        Log.d(TAG, "onHandleWork: got input as: $input")
        backgroundWork()
    }

    private fun backgroundWork() {
        for (i in 0 until 10) {
            Log.d(TAG, "backgroundWork: run: $i")
            if (isStopped) {
                Log.d(TAG, "backgroundWork: Job stopped before completion.")
                return
            }
            SystemClock.sleep(1000)
        }
        Log.d(TAG, "backgroundWork: Task completed.")
    }

    /**
     * In API 26+ JobScheduler is used in JobIntentService. If JobScheduler is stopped by system
     * then this function is triggered.
     *
     * @return boolean, whether job is need to be rescheduled or not.
     * Default value is true.
     * false: current intent and following intents will be dropped and job will be cancelled.
     */
    override fun onStopCurrentWork(): Boolean {
        Log.d(TAG, "onStopCurrentWork: called.")
        return super.onStopCurrentWork()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Job Intent service destroyed.")
    }

    companion object {
        private const val TAG = "JobIntentServiceExample"
        private const val JOB_ID = 123

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, JobIntentServiceExample::class.java, JOB_ID, work)
        }
    }
}