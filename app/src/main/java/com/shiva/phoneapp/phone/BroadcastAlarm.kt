package com.shiva.phoneapp.phone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.shiva.phoneapp.MyForeGroundService
import com.shiva.phoneapp.NotificationUtils


/**
 * Created by Shiva Kishore on 2/6/2022.
 */
class BroadcastAlarm : BroadcastReceiver() {
    override fun onReceive(context: Context, p1: Intent?) {
        Log.d("ALARM", "onrec $p1")
//        val intent = Intent(context, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.applicationContext.startActivity(intent)
//        NotificationUtils.sendNotification(context.applicationContext, "abc")
        startWork(context, "abc")
/*
        val forGroundIntent = Intent(context.applicationContext, MyForeGroundService::class.java)
        context.applicationContext.startForegroundService(forGroundIntent)
*/
    }

    private fun startWork(context: Context, it: String) {
        val notificationData = Data.Builder()
            .putString("message", it)
            .build()

        val work = OneTimeWorkRequest.Builder(ScheduledWorker::class.java)
            .setInputData(notificationData)
            .build()

        // Start Worker
        WorkManager.getInstance(context).beginWith(work).enqueue()
    }
}