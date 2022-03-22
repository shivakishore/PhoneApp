package com.shiva.phoneapp.phone

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.shiva.phoneapp.*

class ScheduledWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        Log.d("WORKER", "dowork")
//        workerParameters.inputData.getString("message")?.let { sendNotification(it) }
//        NotificationUtils.sendNotification(context.applicationContext, "abc")
//        val intent = Intent(context, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.applicationContext.startActivity(intent)

//        val forGroundIntent = Intent(context.applicationContext, MyForeGroundService::class.java)
//        context.applicationContext.startForegroundService(forGroundIntent)
        CallUtils.addNewIncomingCall(context.applicationContext,"9701515051")


        return Result.success()
    }
}
