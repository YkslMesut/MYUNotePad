package com.mesutyukselusta.myunotepad.util
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import io.karn.notify.Notify
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        var message = ""
        when (intent.action) {
            Constants.ACTION_SET_EXACT -> {
                message = intent.getStringExtra("message")!!
                buildNotification(context,message)
            }

            // Not Used
            Constants.ACTION_SET_REPETITIVE_EXACT -> {
                setRepetitiveAlarm(AlarmService(context))
                buildNotification(context, message)
            }
        }
    }

    private fun buildNotification(context: Context,message: String) {
        Notify
            .with(context)
            .content {
                this.title = "MYUNotepad"
                text = message
            }
            .show()
    }

    private fun setRepetitiveAlarm(alarmService: AlarmService) {
        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(7)
            Timber.d("Set alarm for next week same time - ${convertDate(this.timeInMillis)}")
        }
        alarmService.setRepetitiveAlarm(cal.timeInMillis)
    }

    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()

}