package com.mesutyukselusta.myunotepad.util

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mesutyukselusta.myunotepad.R
import com.mesutyukselusta.myunotepad.view.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_ID = "1000"
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        val intent = Intent(context,MainActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context,0,intent,0)

        val builder = NotificationCompat.Builder(context!!, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.app_icon_foreground)
            .setContentTitle("MYUNotepad")
            .setContentText("Content")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(NOTIFICATION_ID,builder.build())
    }
}