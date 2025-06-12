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
class NotificationBuilder(private val service: Service) {
    private val remoteViewsFactory = RemoteViewsFactory(service)

    fun build(): android.app.Notification {
        return NotificationCompat.Builder(service, "ttot")
            .setSmallIcon(R.drawable.tv)
            .setContentText("")
            .setContentTitle("")
            .setOngoing(true)
            .setCustomContentView(remoteViewsFactory.create())
            .build()
    }
}