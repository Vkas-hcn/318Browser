package com.during.festival.rain.falls.one.ad.xian


import com.during.festival.rain.falls.one.appbean.BrowserAllBean

class CanShowH5 {

    private val adFrequencyManager = H5FrequencyManager()

    // 检查是否可以展示广告
    fun canShowAd(jsonBean: BrowserAllBean): Boolean {
        val hData =
            jsonBean.networkConfiguration.webSettings.endpoints.getOrNull(0)?.limits?.hourly ?: 0
        val dayData =
            jsonBean.networkConfiguration.webSettings.endpoints.getOrNull(0)?.limits?.daily ?: 0
        adFrequencyManager.init(maxHourlyShows = hData, maxDailyShows = dayData)
        return adFrequencyManager.canShowAd()
    }

    // 记录广告展示
     fun recordAdShown() {
        adFrequencyManager.recordAdShown()
    }
}
