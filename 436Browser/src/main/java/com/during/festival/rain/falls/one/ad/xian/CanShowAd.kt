package com.during.festival.rain.falls.one.ad.xian


import com.during.festival.rain.falls.one.appbean.BrowserAllBean

class CanShowAd {

    private val adFrequencyManager = AdFrequencyManager()

    // 检查是否可以展示广告
    fun canShowAd(jsonBean: BrowserAllBean,isPostInt:Boolean): Boolean {
        val maxHourlyShows = jsonBean.applicationSettings.exposureControls.displayLimits.hourly
        val maxDailyShows = jsonBean.applicationSettings.exposureControls.displayLimits.daily
        val maxClicks = jsonBean.applicationSettings.exposureControls.interactionLimits
        adFrequencyManager.init(maxHourlyShows, maxDailyShows,maxClicks)
        return adFrequencyManager.canShowAd(isPostInt)
    }

    // 记录广告展示
     fun recordAdShown() {
        adFrequencyManager.recordAdShown()
    }
    fun recordAdClick() {
        adFrequencyManager.recordAdClick()
    }
}
