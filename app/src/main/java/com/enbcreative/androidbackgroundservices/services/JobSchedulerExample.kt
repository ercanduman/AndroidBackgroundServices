package com.enbcreative.androidbackgroundservices.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.enbcreative.androidbackgroundservices.utils.logd
import java.lang.Thread.sleep

class JobSchedulerExample : JobService() {
    private var jobCanceled = false
    override fun onStartJob(params: JobParameters?): Boolean {
        logd("Job Started.")
        backgroundWork(params)
        return true
    }

    private fun backgroundWork(parameters: JobParameters?) {
        Thread {
            for (i in 0 until 10) {
                Log.d(TAG, "backgroundWork: run: $i")
                if (jobCanceled) return@Thread
                sleep(1000)
            }

            Log.d(TAG, "backgroundWork: Job finished")
            /**
             * Should let the system job finished as well. Otherwise app will use a lot of  battery
             */
            jobFinished(parameters, false)
        }.start()
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        logd("Job cancelled before completion.")
        jobCanceled = true
        return true
    }

    companion object {
        private const val TAG = "JobSchedulerExample"
    }
}