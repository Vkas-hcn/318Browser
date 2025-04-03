package com.during.festival.rain.falls.one.ad.xian


import com.during.festival.rain.falls.one.main.IntBorApp
import com.during.festival.rain.falls.one.utils.AppPointData
import com.during.festival.rain.falls.one.utils.TtPoint
import java.text.SimpleDateFormat
import java.util.*

class AdFrequencyManager() {
    private var MAX_HOURLY_SHOWS = 0
    private var MAX_DAILY_SHOWS = 0
    private var MAX_CLICKS = 0

    fun init(maxHourlyShows: Int, maxDailyShows: Int, maxClicks: Int) {
        MAX_HOURLY_SHOWS = maxHourlyShows
        MAX_DAILY_SHOWS = maxDailyShows
        MAX_CLICKS = maxClicks
    }

    // 检查是否可以展示广告
    fun canShowAd(isPostInt: Boolean): Boolean {
        // 检查每日展示限制
        if (!checkDailyShowLimit()) {
            if (isPostInt) {
                TtPoint.postPointData(false, "ispass", "string", "dayShowLimit")
                AppPointData.getLiMitData()
            }
            return false
        }
        // 检查点击限制
        if (!checkClickLimit()) {
            if (isPostInt) {
                TtPoint.postPointData(false, "ispass", "string", "dayClickLimit")
                AppPointData.getLiMitData()
            }

            return false
        }
        // 检查小时限制
        if (!checkHourLimit()) {
            if (isPostInt) {
                TtPoint.postPointData(false, "ispass", "string", "hourShowLimit")
            }
            return false
        }

        return true
    }

    // 记录广告展示
    fun recordAdShown() {
        IntBorApp.showLog("记录插屏广告展示")
        // 更新小时计数
        updateHourCount()
        // 更新每日展示计数
        updateDailyShowCount()
    }

    fun recordAdClick() {
        IntBorApp.showLog("记录插屏广告点击")
        // 更新点击计数
        updateClickCount()
    }

    private fun checkHourLimit(): Boolean {
        val currentHour = getCurrentHourString()
        val lastHour = IntBorApp.okSpBean.adHourShowDate
        val hourCount = IntBorApp.okSpBean.adHourShowNum
        // 如果进入新小时段则重置计数
        if (currentHour != lastHour) {
            IntBorApp.okSpBean.adHourShowDate = currentHour
            IntBorApp.okSpBean.adHourShowNum = 0
            IntBorApp.showLog("插屏-小时展示数重置")
            return true
        }
        IntBorApp.showLog("插屏-小时展示数=$hourCount ----小时最大展示数=${MAX_HOURLY_SHOWS}")
        return hourCount < MAX_HOURLY_SHOWS
    }

    private fun checkDailyShowLimit(): Boolean {
        val currentDate = getCurrentDateString()
        val lastDate = IntBorApp.okSpBean.adDayShowDate
        val dailyCount = IntBorApp.okSpBean.adDayShowNum

        // 如果进入新日期则重置计数
        if (currentDate != lastDate) {
            IntBorApp.okSpBean.adDayShowDate = (currentDate)
            IntBorApp.okSpBean.adDayShowNum = (0)
            IntBorApp.okSpBean.getlimit = false
            IntBorApp.showLog("插屏-天展示数重置")
            return true
        }
        IntBorApp.showLog("插屏-天展示数=$dailyCount ----天最大展示数=${MAX_DAILY_SHOWS}")
        return dailyCount < MAX_DAILY_SHOWS
    }

    private fun checkClickLimit(): Boolean {
        val clickCount = IntBorApp.okSpBean.adClickNum
        IntBorApp.showLog("插屏-点击数=$clickCount ----点击最大展示数=${MAX_CLICKS}")
        return clickCount < MAX_CLICKS
    }

    private fun updateHourCount() {
        val currentHour = getCurrentHourString()
        val lastHour = IntBorApp.okSpBean.adHourShowDate
        if (currentHour == lastHour) {
            val newCount = IntBorApp.okSpBean.adHourShowNum + 1
            IntBorApp.okSpBean.adHourShowNum = (newCount)
        } else {
            IntBorApp.okSpBean.adHourShowDate = (currentHour)
            IntBorApp.okSpBean.adHourShowNum = (1)
        }
    }

    private fun updateDailyShowCount() {
        val currentDate = getCurrentDateString()
        val lastDate = IntBorApp.okSpBean.adDayShowDate
        if (currentDate == lastDate) {
            val newCount = IntBorApp.okSpBean.adDayShowNum + 1
            IntBorApp.okSpBean.adDayShowNum = (newCount)
        } else {
            IntBorApp.okSpBean.adDayShowDate = (currentDate)
            IntBorApp.okSpBean.adDayShowNum = (1)
        }
    }

    private fun updateClickCount() {
        val newCount = IntBorApp.okSpBean.adClickNum + 1
        IntBorApp.okSpBean.adClickNum = (newCount)
    }

    private fun getCurrentHourString() =
        SimpleDateFormat("yyyyMMddHH", Locale.getDefault()).format(Date())

    private fun getCurrentDateString() =
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
}
