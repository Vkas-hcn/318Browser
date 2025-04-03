package com.during.festival.rain.falls.one.main


import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import com.during.festival.rain.falls.one.ui.BroKaActivity
import com.during.festival.rain.falls.one.utils.PngCanGo.KEY_IS_SERVICE
import com.during.festival.rain.falls.one.utils.TtPoint
import com.during.festival.rain.falls.one.utils.PngAllData
import com.during.festival.rain.falls.one.utils.PngCanGo
import com.during.festival.rain.falls.one.zbrofkbee.serbro.BroFService

@Keep
class StartService : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        PngCanGo.addActivity(activity)
        if (!KEY_IS_SERVICE) {
            ContextCompat.startForegroundService(
                activity,
                Intent( activity, BroFService::class.java)
            )
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity is BroKaActivity) {
            return
        }
        if (activity.javaClass.name.contains(PngAllData.reladRu)) {
            IntBorApp.showLog("onActivityStarted=${activity.javaClass.name}")
            val anTime = PngCanGo.getInstallTimeDataFun()
            TtPoint.postPointData(false, "session_front", "time", anTime)
        }
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        PngCanGo.removeActivity(activity)
    }

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
    }
}
