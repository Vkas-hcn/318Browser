package com.during.festival.rain.falls.one.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.during.festival.rain.falls.one.main.IntBorApp.mainStart
import org.json.JSONObject
import java.util.UUID
import com.anythink.core.api.ATAdInfo
import com.appsflyer.AFAdRevenueData
import com.appsflyer.AdRevenueScheme
import com.appsflyer.AppsFlyerLib
import com.appsflyer.MediationNetwork
import com.during.festival.rain.falls.one.main.IntBorApp
import com.facebook.appevents.AppEventsLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Currency

object AppPointData {
    fun showAppVersion(): String {
        return mainStart.packageManager.getPackageInfo(mainStart.packageName, 0).versionName ?: ""
    }

    private fun topJsonData(context: Context): JSONObject {
        val is_android = IntBorApp.okSpBean.appiddata

        val mig = JSONObject().apply {
            //manufacturer
            put("expedite", Build.MANUFACTURER)
            //gaid
            put("lottery", "")
        }


        val aurora = JSONObject().apply {
            //distinct_id
            put("gunpoint", is_android)

            //log_id
            put("royalty", UUID.randomUUID().toString())
            //app_version
            put("canberra", showAppVersion())

        }

        val calabash = JSONObject().apply {
            //bundle_id
            put("shopworn", context.packageName)
            //os
            put("electret", "spangle")
            //client_ts
            put("prop", System.currentTimeMillis())
            //android_id
            put("maurine", is_android)
            //os_version
            put("tamarind", Build.VERSION.RELEASE)
            //operator 传假值字符串
            put("fractal", "778899")
            //system_language//假值
            put("adoption", "ceasd_vrd")
            //device_model-最新需要传真实值
            put("coffer", Build.BRAND)
        }

        val json = JSONObject().apply {
            put("mig", mig)
            put("aurora", aurora)
            put("calabash", calabash)
        }

        return json
    }

    fun upInstallJson(context: Context): String {
        val is_ref = IntBorApp.okSpBean.refdata
        val nil = JSONObject().apply {
            //build
            put("wigmake", "build/${Build.ID}")

            //referrer_url
            put("axle", is_ref)

            //user_agent
            put("actinic", "")

            //lat
            put("violate", "calibre")

            //referrer_click_timestamp_seconds
            put("soothe", 0)

            //install_begin_timestamp_seconds
            put("morsel", 0)

            //referrer_click_timestamp_server_seconds
            put("edgar", 0)

            //install_begin_timestamp_server_seconds
            put("bayonet", 0)

            //install_first_seconds
            put("hairdo", getFirstInstallTime(context))

            //last_update_seconds
            put("oxalic", 0)
        }
        return topJsonData(context).apply {
            put("nil",nil)
        }.toString()
    }

    fun upAdJson(context: Context, adValue: ATAdInfo): String {
        val honey = JSONObject().apply {
            //ad_pre_ecpm
            put("settle", adValue.publisherRevenue * 1000000)
            //currency
            put("waterloo", "USD")
            //ad_network
            put(
                "inure",
                adValue.adsourceId
            )
            //ad_source
            put("detonate", "TopOn")
            //ad_code_id
            put("matisse", adValue.placementId)
            //ad_pos_id
            put("sax", "int")

            //ad_rit_id
            put("connive", "")
            //ad_sense
            put("go", "")
            //ad_format
            put("hart", adValue.format)
        }
        return topJsonData(context).apply {
            put("honey",honey)

        }.toString()
    }

    fun upPointJson(name: String): String {
        return topJsonData(mainStart).apply {
            put("penitent", name)
        }.toString()
    }

    fun upPointJson(
        name: String,
        key1: String? = null,
        keyValue1: Any? = null,
        key2: String? = null,
        keyValue2: Any? = null,
    ): String {
        val gregory = JSONObject().apply {
            if (key1 != null) {
                put(key1, keyValue1)
            }
            if (key2 != null) {
                put(key2, keyValue2)
            }
        }
        return topJsonData(mainStart).apply {
            put("penitent", name)
            put("gregory", gregory)

        }.toString()
    }
    private fun getFirstInstallTime(context: Context): Long {
        try {
            val packageInfo =
                context.packageManager.getPackageInfo(context.packageName, 0)
            return packageInfo.firstInstallTime / 1000
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }


     fun postAdValue(adValue: ATAdInfo) {
        val ecmVVVV = try {
            adValue.publisherRevenue
        } catch (e: NumberFormatException) {
            IntBorApp.showLog("Invalid ecpmPrecision value: ${adValue.ecpmPrecision}, using default value 0.0")
            0.0
        }
        val adRevenueData = AFAdRevenueData(
            adValue.adsourceId,
            MediationNetwork.TOPON,
            "USD",
            ecmVVVV
        )
        val additionalParameters: MutableMap<String, Any> = HashMap()
        additionalParameters[AdRevenueScheme.AD_UNIT] = adValue.placementId
        additionalParameters[AdRevenueScheme.AD_TYPE] = "Interstitial"
        AppsFlyerLib.getInstance().logAdRevenue(adRevenueData, additionalParameters)

        val jsonBean = IntBorApp.getAdminData()
        val data = jsonBean?.assetManagement?.identifiers?.socialId ?: ""
         val fbId = data.split("_")[0]
        if (fbId.isBlank()) {
            return
        }
        if (fbId.isNotEmpty()) {
            try {
                AppEventsLogger.newLogger(mainStart).logPurchase(
                    BigDecimal(ecmVVVV.toString()),
                    Currency.getInstance("USD")
                )
            } catch (e: NumberFormatException) {
                IntBorApp.showLog("Invalid ecpmPrecision value: ${adValue.ecpmPrecision}, skipping logPurchase")
            }
        }
    }


    fun getadmin(userCategory: Int, codeInt: String?) {
        var isuserData: String? = null

        if (codeInt == null) {
            isuserData = null
        } else if (codeInt != "200") {
            isuserData = codeInt
        } else if (userCategory==1) {
            isuserData = "a"
        } else {
            isuserData = "b"
        }

        TtPoint.postPointData(true, "getadmin", "getstring", isuserData)
    }


    fun showsuccessPoint() {
        val time = (System.currentTimeMillis() - IntBorApp.showAdTime) / 1000
        TtPoint.postPointData(false, "show", "t", time)
        IntBorApp.showAdTime = 0
    }

    fun firstExternalBombPoint() {
        val ata = IntBorApp.okSpBean.firstPoint
        if (ata) {
            return
        }
        val instalTime = PngCanGo.getInstallTimeDataFun()
        TtPoint.postPointData(true, "first_start", "time", instalTime)
        IntBorApp.okSpBean.firstPoint = true
    }

    fun pointInstallAf(data: String) {
        val keyIsAdOrg = IntBorApp.okSpBean.adOrgPoint
        if (data.contains("non_organic", true) && !keyIsAdOrg) {
            TtPoint.postPointData(true, "non_organic")
            IntBorApp.okSpBean.adOrgPoint = true
        }
    }

    fun getLiMitData() {
        val getlimitState = IntBorApp.okSpBean.getlimit
        if (!getlimitState) {
            TtPoint.postPointData(false, "getlimit")
            IntBorApp.okSpBean.getlimit = true
        }
    }
    fun sessionUp() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                TtPoint.postPointData(false, "session_up")
                delay(1000 * 60 * 15)
            }
        }
    }
}