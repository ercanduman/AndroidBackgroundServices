package com.enbcreative.androidbackgroundservices.ui

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.enbcreative.androidbackgroundservices.JOB_ID
import com.enbcreative.androidbackgroundservices.R
import com.enbcreative.androidbackgroundservices.services.ForegroundServiceExample
import com.enbcreative.androidbackgroundservices.services.IntentServiceExample
import com.enbcreative.androidbackgroundservices.services.JobIntentServiceExample
import com.enbcreative.androidbackgroundservices.services.JobSchedulerExample
import com.enbcreative.androidbackgroundservices.utils.logd
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        fab_main.setOnClickListener { view ->
            Snackbar.make(view, getString(R.string.stop_services), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.stop)) {
                    stopJobScheduler()
                    stopMyForegroundService()
                    stopIntentService()
                }.show()
        }
    }

    private fun startJobScheduler() {
        logd("Job scheduler start clicked.")

        val componentName = ComponentName(this, JobSchedulerExample::class.java)
        val jobInfo = JobInfo.Builder(JOB_ID, componentName)
            .setRequiresCharging(true)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
            .setPeriodic(15 * 60 * 1000) // 15 mins
            .build()

        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = jobScheduler.schedule(jobInfo)

        if (resultCode == JobScheduler.RESULT_SUCCESS) logd("Job scheduled.")
        else logd("Job scheduling failed.")
    }

    private fun stopJobScheduler() {
        logd("Job scheduler stop  clicked.")
        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(JOB_ID)
        logd("Job cancelled.")
    }

    private fun startMyForegroundService() {
        val input = edt_input_text.text.toString()

        Intent(this, ForegroundServiceExample::class.java).apply {
            putExtra(KEY_INPUT, input)
            ContextCompat.startForegroundService(this@MainActivity, this)
        }
    }

    private fun stopMyForegroundService() {
        Intent(this, ForegroundServiceExample::class.java).apply {
            stopService(this)
        }
    }

    private fun startIntentService() {
        val input = edt_input_text.text.toString()
        Intent(this, IntentServiceExample::class.java).apply {
            putExtra(KEY_INPUT, input)
            ContextCompat.startForegroundService(this@MainActivity, this)
        }
    }

    private fun stopIntentService() {
        Intent(this, IntentServiceExample::class.java).apply {
            stopService(this)
        }
    }

    private fun startJobIntentService() {
        Intent(this, JobIntentServiceExample::class.java).apply {
            putExtra(KEY_INPUT, edt_input_text.text.toString())
            JobIntentServiceExample.enqueueWork(this@MainActivity, this)
        }
    }

    companion object {
        const val KEY_INPUT = "KEY_INPUT"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.start_job_scheduler -> {
                startJobScheduler()
                true
            }
            R.id.start_foreground_service -> {
                startMyForegroundService()
                true
            }
            R.id.start_intent_service -> {
                startIntentService()
                true
            }
            R.id.start_job_intent_service -> {
                startJobIntentService()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
}