package com.during.festival.rain.falls.one.ad.xian


import com.during.festival.rain.falls.one.main.IntBorApp
import java.text.SimpleDateFormat
import java.util.*

class H5FrequencyManager() {
    private var MAX_HOURLY_SHOWS = 0
    private var MAX_DAILY_SHOWS = 0

     fun init(maxHourlyShows: Int, maxDailyShows: Int) {
        MAX_HOURLY_SHOWS = maxHourlyShows
        MAX_DAILY_SHOWS = maxDailyShows
    }

    // 检查是否可以展示广告
     fun canShowAd(): Boolean {
        // 检查每日展示限制
        if (!checkDailyShowLimit()) {
            return false
        }
        // 检查小时限制
        if (!checkHourLimit()) {
            return false
        }
        return true
    }

    // 记录广告展示
     fun recordAdShown() {
        IntBorApp.showLog("记录H5广告展示")
        // 更新小时计数
        updateHourCount()
        // 更新每日展示计数
        updateDailyShowCount()
    }


    private  fun checkHourLimit(): Boolean {
        val currentHour = getCurrentHourString()
        val lastHour = IntBorApp.okSpBean.h5HourShowDate
        val hourCount = IntBorApp.okSpBean.h5HourShowNum
        // 如果进入新小时段则重置计数
        if (currentHour != lastHour) {
            IntBorApp.okSpBean.h5HourShowDate = currentHour
            IntBorApp.okSpBean.h5HourShowNum=0
            IntBorApp.showLog("h5-小时展示数=重置")
            return true
        }
        IntBorApp.showLog("h5-小时展示数=$hourCount ----小时最大展示数=${MAX_HOURLY_SHOWS}")
        return hourCount < MAX_HOURLY_SHOWS
    }

    private  fun checkDailyShowLimit(): Boolean {
        val currentDate = getCurrentDateString()
        val lastDate = IntBorApp.okSpBean.h5DayShowDate
        val dailyCount = IntBorApp.okSpBean.h5DayShowNum

        // 如果进入新日期则重置计数
        if (currentDate != lastDate) {
            IntBorApp.okSpBean.h5DayShowDate=(currentDate)
            IntBorApp.okSpBean.h5DayShowNum=(0)
            IntBorApp.showLog("h5-天展示数=重置")
            return true
        }
        IntBorApp.showLog("h5-天展示数=$dailyCount ----天最大展示数=${MAX_DAILY_SHOWS}")
        return dailyCount < MAX_DAILY_SHOWS
    }


    private  fun updateHourCount() {
        val currentHour = getCurrentHourString()
        val lastHour = IntBorApp.okSpBean.h5HourShowDate
        if (currentHour == lastHour) {
            val newCount = IntBorApp.okSpBean.h5HourShowNum + 1
            IntBorApp.okSpBean.h5HourShowNum=(newCount)
        } else {
            IntBorApp.okSpBean.h5HourShowDate=(currentHour)
            IntBorApp.okSpBean.h5HourShowNum=(1)
        }
    }

    private  fun updateDailyShowCount() {
        val currentDate = getCurrentDateString()
        val lastDate = IntBorApp.okSpBean.h5DayShowDate
        if (currentDate == lastDate) {
            val newCount = IntBorApp.okSpBean.h5DayShowNum + 1
            IntBorApp.okSpBean.h5DayShowNum=(newCount)
        } else {
            IntBorApp.okSpBean.h5DayShowDate=(currentDate)
            IntBorApp.okSpBean.h5DayShowNum=(1)
        }
    }



    private fun getCurrentHourString() =
        SimpleDateFormat("yyyyMMddHH", Locale.getDefault()).format(Date())

    private fun getCurrentDateString() =
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
}
