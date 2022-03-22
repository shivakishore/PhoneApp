package com.shiva.phoneapp

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * Created by Shiva Kishore on 2/12/2022.
 */
class MyForeGroundService : Service() {
    private val binder = LocalBinder()

    override fun onCreate() {
        startForeground(
            NotificationUtils.INCALL_ID,
            NotificationUtils.getInCallNotification(applicationContext, "Ongoing Calling")
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods
        fun getService(): MyForeGroundService = this@MyForeGroundService
    }

    override fun onDestroy() {
        super.onDestroy()
        NotificationUtils.cancelInCallNotification(applicationContext)
    }
}