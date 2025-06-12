package bigo.ad.zces

import android.app.NotificationChannel
import android.app.NotificationManager

class NotificationChannelFactory {
    fun create(): NotificationChannel {
        return NotificationChannel("ttot", "ttot", NotificationManager.IMPORTANCE_MIN).apply {
            setSound(null, null)
            enableLights(false)
            enableVibration(false)
        }
    }
}