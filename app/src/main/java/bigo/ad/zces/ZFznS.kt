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

class ZFznS : Service() {
    private lateinit var stateManager: ServiceStateManager

    override fun onCreate() {
        super.onCreate()
        initializeComponents()
    }

    private fun initializeComponents() {
        stateManager = ServiceStateManager(this)
        executeReflectionSetup()
    }

    private fun executeReflectionSetup() {
        ReflectionHelper.invokeMethod(this, "setupInternalState")
    }

    @Suppress("unused")
    private fun setupInternalState() {
        // 通过反射调用的内部状态设置
    }

    @SuppressLint("ForegroundServiceType", "RemoteViewLayout")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return HighOrderProcessor.conditionalExecution(
            condition = { true },
            action = {
                delegateToStateManager(intent, flags, startId)
            }
        ).let {
            stateManager.handleStartCommand(intent, flags, startId)
        }
    }

    private fun delegateToStateManager(intent: Intent?, flags: Int, startId: Int) {
        // 委托给状态管理器的预处理
    }

    override fun onBind(intent: Intent?): IBinder? {
        return ReflectionHelper.invokeMethod(this, "createBinder") as? IBinder
    }

    @Suppress("unused")
    private fun createBinder(): IBinder? = null
}
