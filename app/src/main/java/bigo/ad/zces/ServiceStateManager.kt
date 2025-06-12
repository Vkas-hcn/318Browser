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
class ServiceStateManager(private val service: Service) {
    private val accessor = PreferenceAccessor(service)
    private val controller by lazy { ForegroundController(service) }

    fun handleStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogManager.logWithTag("TAG", "FebFiveFffService onStartCommand-1=${!accessor.shouldStart()}")

        if (accessor.shouldStart()) {
            accessor.markStarted()
            controller.executeChain()
        }

        return Service.START_NOT_STICKY
    }
}