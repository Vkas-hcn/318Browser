package com.during.festival.rain.falls.one.ad

import android.app.KeyguardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.interstitial.api.ATInterstitial
import com.anythink.interstitial.api.ATInterstitialListener
import com.during.festival.rain.falls.one.utils.PngCanGo
import com.during.festival.rain.falls.one.utils.TtPoint
import com.during.festival.rain.falls.one.utils.AppPointData
import com.during.festival.rain.falls.one.main.IntBorApp
import com.during.festival.rain.falls.one.main.IntBorApp.adLimiter
import com.during.festival.rain.falls.one.main.IntBorApp.mainStart
import com.during.festival.rain.falls.one.zbrokloe.BrowserLoad
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class AdManager {
    private var adTimeoutJob: Job? = null

    // 广告对象
    lateinit var interstitialAd: ATInterstitial

    // 广告缓存时间（单位：毫秒）
    private val AD_CACHE_DURATION = 50 * 60 * 1000L // 50分钟

    // 上次广告加载时间
    private var lastAdLoadTime: Long = 0

    // 是否正在加载广告
    private var isLoading = false
    var canNextState = false
    var clickState = false
    var isHaveAdData = false

    // 广告初始化，状态回调
    fun initAd() {
        val idBean = IntBorApp.getAdminData() ?: return
        val id = idBean.assetManagement.identifiers.internalId
        IntBorApp.showLog("体外广告id=: ${id}")
        // 创建插屏广告对象
        interstitialAd = ATInterstitial(IntBorApp.mainStart, id)
        // 设置插屏广告监听器
        interstitialAd.setAdListener(object : ATInterstitialListener {
            override fun onInterstitialAdLoaded() {
                IntBorApp.showLog("体外广告onAdLoaded: 广告加载成功")
                lastAdLoadTime = System.currentTimeMillis()
                isLoading = false
                TtPoint.postPointData(false, "getadvertise")
                isHaveAdData = true
            }

            override fun onInterstitialAdLoadFail(p0: AdError?) {
                IntBorApp.showLog("体外广告onAdFailed: 广告加载失败=${p0?.fullErrorInfo}")
                resetAdStatus()
                TtPoint.postPointData(
                    false,
                    "getfail",
                    "string1",
                    p0?.fullErrorInfo
                )
                isHaveAdData = false
            }

            override fun onInterstitialAdClicked(p0: ATAdInfo?) {
                IntBorApp.showLog("体外广告onAdClicked: 广告${p0?.getPlacementId()}被点击")
                adLimiter.recordAdClick()
                clickState = true
            }

            override fun onInterstitialAdShow(p0: ATAdInfo?) {
                IntBorApp.adShowTime = System.currentTimeMillis()
                clickState = false
                IntBorApp.showLog("体外广告onAdImpression: 广告${p0?.getPlacementId()}展示")
                adLimiter.recordAdShown()
                isHaveAdData = false
                resetAdStatus()
                p0?.let { TtPoint.postAdData(it) }
                AppPointData.showsuccessPoint()
                loadAd()
            }

            override fun onInterstitialAdClose(p0: ATAdInfo?) {
                IntBorApp.showLog("体外广告onAdClosed: 广告${p0?.getPlacementId()}被关闭")
                PngCanGo.closeAllActivities()
//                cloneAndOpenH5()
            }

            override fun onInterstitialAdVideoStart(p0: ATAdInfo?) {
            }

            override fun onInterstitialAdVideoEnd(p0: ATAdInfo?) {
            }

            override fun onInterstitialAdVideoError(p0: AdError?) {
                resetAdStatus()
                IntBorApp.showLog("体外广告onAdClosed: 广告展示失败")
                TtPoint.postPointData(
                    false,
                    "showfailer",
                    "string3",
                    p0?.fullErrorInfo
                )
            }
        })
    }

    fun loadAd() {
        if (AdLimiter.canShowAd(false)) {
            IntBorApp.showLog("体外广告展示限制,不加载广告")
            return
        }
        val currentTime = System.currentTimeMillis()
        if (isHaveAdData && ((currentTime - lastAdLoadTime) < AD_CACHE_DURATION)) {
            IntBorApp.showLog("不加载,有缓存的广告")
        } else {
            if (((currentTime - lastAdLoadTime) >= AD_CACHE_DURATION)&&!isLoading) {
                IntBorApp.showLog("无广告加载，或者缓存过期重新加载")
                resetAdStatus()
            }
            if (isLoading) {
                IntBorApp.showLog("正在加载广告，等待加载完成")
                return
            }
            isLoading = true
            IntBorApp.showLog("发起新的广告请求")
            interstitialAd.load()
            TtPoint.postPointData(false, "reqadvertise")
            startAdTimeout()
        }
    }
    private fun startAdTimeout() {
        cancelTimeout()
        adTimeoutJob = CoroutineScope(Dispatchers.IO).launch {
            delay(60_000)
            if (isLoading && !isHaveAdData) {
                IntBorApp.showLog("广告加载超时，重新请求广告")
                resetAdStatus()
                loadAd()
            }
        }
    }

    private fun cancelTimeout() {
        adTimeoutJob?.cancel()
        adTimeoutJob = null
    }

    //广告状态重置
    fun resetAdStatus() {
        isLoading = false
        lastAdLoadTime = 0
        isHaveAdData = false
    }

//    private fun cloneAndOpenH5() {
//        // 锁屏+亮屏幕  && 在N秒后 && H5不超限
//        // 当广告未关闭 下一个循环触发 体外广告，也会调用Close
//        val jsonBean = IntBorApp.getAdminData() ?: return
//        val h5Url = jsonBean.networkConfiguration.webSettings.endpoints.getOrNull(0)?.url ?: ""
//        IntBorApp.showLog("h5Url=: ${h5Url}=clickState=${clickState}")
//        if (clickState || h5Url.isEmpty()) {
//            return
//        }
//        clickState = false
//        IntBorApp.showLog("关闭打开H5")
//        if (canShowLocked()) {
//            IntBorApp.showLog("锁屏或者息屏状态，广告不展示")
//            return
//        }
//        val installFast = PngCanGo.getInstallFast()
//        val ins = jsonBean.applicationSettings.schedule.checkIntervals.getOrNull(1) ?: 0
//        val wait = jsonBean.applicationSettings.schedule.checkIntervals.getOrNull(2) ?: 0
//
//        val timeD = installFast + (ins * 1000) + (jsonBean.networkConfiguration.webSettings.initialDelay * 1000)
//        val jiange = (System.currentTimeMillis() - IntBorApp.adShowTime) / 1000
//        IntBorApp.showLog("H5----1---=${timeD <= System.currentTimeMillis()}")
//        IntBorApp.showLog("H5----2---=${jiange < wait}")
//
//        if (timeD <= System.currentTimeMillis() && jiange < wait) {
//            // 检查广告展示限制
//            if (!IntBorApp.h5Limiter.canShowAd(jsonBean)) {
//                IntBorApp.showLog("h5广告展示限制")
//                return
//            }
//            IntBorApp.showLog("跳转打开H5")
//            PngCanGo.isH5State = true
//            canNextState = true
//            showAdAndTrack2()
//        }
//    }

//    private fun showAdAndTrack2() {
//        CoroutineScope(Dispatchers.Main).launch {
//            addFa()
//            PngCanGo.closeAllActivities()
//            delay(678)
//            BrowserLoad.bload("ispsasscspasgi", false)
//            TtPoint.postPointData(false, "callstart")
//        }
//    }

    fun addFa() {
        val okSpBean = IntBorApp.okSpBean ?: return
        synchronized(okSpBean) {
            var adNum = okSpBean.isAdFailCount
            adNum++
            okSpBean.isAdFailCount = adNum
        }
    }

    fun isHaveAdNextFun() {
        // 检查锁屏或息屏状态，避免过多的嵌套
        if (canShowLocked()) {
            IntBorApp.showLog("锁屏或者息屏状态，广告不展示")
            return
        }
        // 调用点位数据函数
        TtPoint.postPointData(false, "isunlock")

        // 获取管理员数据
        val jsonBean = IntBorApp.getAdminData() ?: return

        // 获取安装时间
        val instalTime = PngCanGo.getInstallTimeDataFun()
        val ins = jsonBean.applicationSettings.schedule.checkIntervals.getOrNull(1) ?: 0
        val wait = jsonBean.applicationSettings.schedule.checkIntervals.getOrNull(2) ?: 0

        // 检查首次安装时间和广告展示时间间隔
        if (AdUtils.isBeforeInstallTime(instalTime, ins)) return
        if (AdUtils.isAdDisplayIntervalTooShort(wait)) return
        val installFast = PngCanGo.getInstallFast()

//        val timeD = installFast + (ins * 1000) + (jsonBean.networkConfiguration.webSettings.initialDelay * 1000)
        canNextState = false
//        val h5Url = jsonBean.networkConfiguration.webSettings.endpoints.getOrNull(0)?.url ?: ""
//        IntBorApp.showLog("h5Url=: ${h5Url}==${timeD > System.currentTimeMillis() && h5Url.isNotEmpty()}")
//        if (timeD > System.currentTimeMillis() && h5Url.isNotEmpty()) {
//            // 检查广告展示限制
//            if (!IntBorApp.h5Limiter.canShowAd(jsonBean)) {
//                IntBorApp.showLog("h5广告展示限制")
//                return
//            }
//            IntBorApp.showLog("h5流程")
//            PngCanGo.isH5State = true
//        } else {
//            PngCanGo.isH5State = false
        if (AdLimiter.canShowAd(true)) {
            IntBorApp.showLog("体外广告展示限制")
            return
        }
        IntBorApp.showLog("体外流程")
//        }
        AdUtils.showAdAndTrack(this)
    }

    fun canShowLocked(): Boolean {
        val powerManager = mainStart.getSystemService(Context.POWER_SERVICE) as? PowerManager
        val keyguardManager =
            mainStart.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
        if (powerManager == null || keyguardManager == null) {
            return false
        }
        val isScreenOn = powerManager.isInteractive
        val isInKeyguardRestrictedInputMode = keyguardManager.inKeyguardRestrictedInputMode()

        return !isScreenOn || isInKeyguardRestrictedInputMode
    }

}
