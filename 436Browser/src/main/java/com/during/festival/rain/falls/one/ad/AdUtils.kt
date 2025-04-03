package com.during.festival.rain.falls.one.ad


import com.during.festival.rain.falls.one.utils.PngCanGo
import com.during.festival.rain.falls.one.utils.TtPoint
import com.during.festival.rain.falls.one.main.IntBorApp
import com.during.festival.rain.falls.one.zbrokloe.BrowserLoad
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object AdUtils {
    fun isBeforeInstallTime(instalTime: Long, ins: Int): Boolean {
        try {
            if (instalTime < ins) {
                IntBorApp.showLog("距离首次安装时间小于$ins 秒，广告不能展示")
                TtPoint.postPointData(false, "ispass", "string", "firstInstallation")
                return true
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }

    fun isAdDisplayIntervalTooShort(wait: Int): Boolean {
        try {
            val jiange = (System.currentTimeMillis() - IntBorApp.adShowTime) / 1000
            if (jiange < wait) {
                IntBorApp.showLog("广告展示间隔时间小于$wait 秒，不展示")
                TtPoint.postPointData(false, "ispass", "string", "Interval")
                return true
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    fun showAdAndTrack(adManager: AdManager) {
        TtPoint.postPointData(false, "ispass", "string", "")
        CoroutineScope(Dispatchers.Main).launch {
            PngCanGo.closeAllActivities()
            delay(1011)
            if (adManager.canNextState) {
                IntBorApp.showLog("准备显示h5广告，中断体外广告")
                return@launch
            }
            adManager.addFa()
            BrowserLoad.bload("callstartgicallstart", true)
            TtPoint.postPointData(false, "callstart")
        }
    }
}
