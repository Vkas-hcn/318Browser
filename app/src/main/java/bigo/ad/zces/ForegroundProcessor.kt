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
class ForegroundProcessor(private val service: Service) {
    fun process() {
        LogManager.logWithTag("TAG", "FebFiveFffService onStartCommand-2=${PreferencesManager(service).startSS}")
    }
}