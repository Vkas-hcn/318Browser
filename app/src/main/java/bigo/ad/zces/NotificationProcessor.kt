package bigo.ad.zces
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import android.widget.RemoteViews
import com.spring.breeze.proud.horse.fast.R
import com.spring.breeze.proud.horse.fast.sp.PreferencesManager
import kotlin.reflect.KClass
class NotificationProcessor(private val service: Service) {
    private val builder = NotificationBuilder(service)

    fun process() {
        val notification = builder.build()
        startForegroundSafely(notification)
    }

    private fun startForegroundSafely(notification: android.app.Notification) {
        runCatching {
            service.startForeground(4434, notification)
        }
    }
}