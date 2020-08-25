package com.enbcreative.androidbackgroundservices.ui

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.enbcreative.androidbackgroundservices.JOB_ID
import com.enbcreative.androidbackgroundservices.R
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
                .setAction(getString(R.string.stop)) { stopJobScheduler() }.show()
        }
        button_job_scheduler.setOnClickListener { startJobScheduler() }
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}