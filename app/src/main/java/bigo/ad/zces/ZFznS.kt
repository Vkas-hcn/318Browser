package bigo.ad.zces

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import android.widget.RemoteViews
import com.spring.breeze.proud.horse.fast.R
import com.spring.breeze.proud.horse.fast.cenklaj.cesa.MainApp
import com.spring.breeze.proud.horse.fast.sp.PreferencesManager

class ZFznS : Service() {

    override fun onCreate() {
        super.onCreate()
    }

    @SuppressLint("ForegroundServiceType", "RemoteViewLayout")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val prefs = PreferencesManager(MainApp.application)

        if (!prefs.startSS) {
            prefs.startSS = true
            ChannelFun.executeChannelFun(MainApp.application)
            runCatching {
                ChannelFun.getOQR(this@ZFznS)
            }
            Log.e("TAG", "onStartCommand: ${prefs.startSS}")
        }
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
