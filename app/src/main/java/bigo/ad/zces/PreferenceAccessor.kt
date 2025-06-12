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
class PreferenceAccessor(private val service: Service) {
    private val prefs by lazy { PreferencesManager(service) }

    fun shouldStart(): Boolean = !prefs.startSS

    fun markStarted() {
        prefs.startSS = true
    }
}