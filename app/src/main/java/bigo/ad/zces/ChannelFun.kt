package bigo.ad.zces

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.spring.breeze.proud.horse.fast.R

object ChannelFun {

    private const val CHANNEL_ID = "notification_channel_id"
    private const val CHANNEL_NAME = "Default Notification Channel"

    fun executeChannelFun(application: Application) {
        val notificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_MIN).apply {
            setSound(null, null)
            enableLights(false)
            enableVibration(false)
        }
        if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("ForegroundServiceType")
    fun getOQR(service: ZFznS){
        service.startForeground(
            9527,
            NotificationCompat.Builder(service, "bghyuj").setSmallIcon(R.drawable.tv)
                .setContentText("")
                .setContentTitle("")
                .setOngoing(true)
                .setCustomContentView(RemoteViews(service.packageName, R.layout.lln))
                .build()
        )
    }
}