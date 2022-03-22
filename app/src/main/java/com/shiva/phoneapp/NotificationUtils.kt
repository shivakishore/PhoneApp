package com.shiva.phoneapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.shiva.phoneapp.CallUtils.Companion.ACTION_ANSWER_CALL
import com.shiva.phoneapp.CallUtils.Companion.ACTION_REJECT_CALL
import com.shiva.phoneapp.phone.MyService
import com.shiva.phoneapp.ui.MainActivity

/**
 * Created by Shiva Kishore on 2/12/2022.
 */
class NotificationUtils {
    companion object {
        const val NEWCALL_ID: Int = 123
        const val NEWCALL_CHANNELID: String = "123"
        const val INCALL_ID: Int = 1234
        const val INCALL_CHANNELID: String = "1234"

        fun getInCallNotification(context: Context, messageBody: String): Notification {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("message", messageBody)
            val pendingIntent = PendingIntent.getActivity(
                context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT
            )

            val rejectIntent = Intent(
                ACTION_REJECT_CALL, null, context,
                MainActivity::class.java
            )
            rejectIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            var notificationBuilder: NotificationCompat.Builder? = null
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(
                INCALL_CHANNELID,
                "Ongoing calls",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = context.packageName
            notificationManager.createNotificationChannel(channel)
            if (notificationBuilder == null) {
                notificationBuilder = NotificationCompat.Builder(context, context.packageName)
            }
            notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(messageBody)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .setChannelId(INCALL_CHANNELID)
                .addAction(
                    NotificationCompat.Action.Builder(
                        IconCompat.createFromIcon(
                            context,
                            Icon.createWithResource(context, android.R.drawable.ic_dialog_alert)
                        ), "End Call",
                        PendingIntent.getActivity(
                            context,
                            0,
                            rejectIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    )
                        .build()
                )

//            notificationManager.notify(0 /* ID of notification */, )
            return notificationBuilder.build()
        }

        fun cancelInCallNotification(context: Context) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(INCALL_ID)
        }

        fun cancelCallNotification(context: Context) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
        }

        fun showIncomingCallNotification(context: Context) {

            val channel = NotificationChannel(
                NEWCALL_CHANNELID, "Incoming Calls",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.importance = NotificationManager.IMPORTANCE_HIGH
            val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            channel.setSound(
                ringtoneUri,
                AudioAttributes.Builder() // Setting the AudioAttributes is important as it identifies the purpose of your
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )

            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.flags = Intent.FLAG_ACTIVITY_NO_USER_ACTION or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setClass(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 1, intent, 0)

            val defaultSoundUri: Uri = if (MyService.callConnection.state == 4) {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            } else {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            }

            val builder = NotificationCompat.Builder(context, "channel")
            builder.setOngoing(true)
            builder.priority = NotificationCompat.PRIORITY_HIGH
            builder.setContentIntent(pendingIntent)
            builder.setFullScreenIntent(pendingIntent, true)
            builder.setSmallIcon(R.mipmap.ic_launcher)
            builder.setContentTitle("Your notification title")
            builder.setContentText("Your notification content.")
            builder.setChannelId(NEWCALL_CHANNELID)
            builder.setSound(defaultSoundUri)
            builder.setVibrate(
                longArrayOf(
                    1000,
                    500,
                    1000,
                    500,
                    1000,
                    500,
                    1000,
                    500,
                    1000,
                    500,
                    1000,
                    500,
                    1000,
                    500,
                    1000,
                    500,
                    1000,
                    500,
                )
            )

            val answerIntent = Intent(
                ACTION_ANSWER_CALL, null, context,
                MainActivity::class.java
            )
            val rejectIntent = Intent(
                ACTION_REJECT_CALL, null, context,
                MainActivity::class.java
            )
            builder.addAction(
                NotificationCompat.Action.Builder(
                    IconCompat.createFromIcon(
                        context,
                        Icon.createWithResource(context, android.R.drawable.ic_dialog_alert)
                    ),
                    "Answer",
                    PendingIntent.getActivity(
                        context, 0, answerIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                    .build()
            )
            builder.addAction(
                NotificationCompat.Action.Builder(
                    IconCompat.createFromIcon(
                        context,
                        Icon.createWithResource(context, android.R.drawable.ic_dialog_alert)
                    ), "Reject",
                    PendingIntent.getActivity(
                        context,
                        0,
                        rejectIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                    .build()
            )

            val notification = builder.build()
            notification.flags = notification.flags or Notification.FLAG_INSISTENT

            val notificationManager: NotificationManager = context.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(NEWCALL_ID, notification)

            //        context.startActivity(intent);

        }

    }
}