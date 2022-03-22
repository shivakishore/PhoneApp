package com.shiva.phoneapp.phone

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by Shiva Kishore on 2/6/2022.
 */
class FirebaseService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "onMessageReceived: ${remoteMessage.data}")

        if (remoteMessage.data.isNotEmpty()) {
            val extras = Bundle()
            for ((key, value) in remoteMessage.data) {
                Log.d(TAG,"remote message $key $value")
                extras.putString(key, value)
            }
            if (extras.containsKey("message") && !extras.getString("message").isNullOrBlank()) {
                registerAlarm(extras.getString("message")!!)
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // If required send token to your app server.
    }

    private fun registerAlarm(messegeBody: String) {
        Log.d(TAG, "registerAlarm: $messegeBody")
        val launchTime = System.currentTimeMillis() + 1000
        val am = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(applicationContext, BroadcastAlarm::class.java)
        i.putExtra("message", messegeBody)
        val pi = PendingIntent.getBroadcast(applicationContext, 0, i, 0)
        am.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            launchTime,
            pi
        )
    }

    companion object {
        private const val TAG = "FcmMessageService"
    }
}