package com.during.festival.rain.falls.one.ui

import android.webkit.WebViewClient
import com.during.festival.rain.falls.one.appbean.BrowserAllBean
import com.during.festival.rain.falls.one.main.IntBorApp
import com.during.festival.rain.falls.one.utils.PngAllData
import com.during.festival.rain.falls.one.utils.TtPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LabuPresenter(
    private val view: LabuContract.View,
    private val adminDataProvider: () -> BrowserAllBean?
) : LabuContract.Presenter {

    override fun initWebConfiguration() {
        adminDataProvider()?.let { data ->
            configureUserAgent(data)
            view.setupWebClient(WebViewClient())
        } ?: IntBorApp.showLog("Admin data is null")
    }

    private fun configureUserAgent(data: BrowserAllBean) {
        val packageName = data.networkConfiguration.webSettings.packageName
        view.configureWebSettings(packageName)
        logConfiguration(packageName)
    }

    override fun loadH5Content() {
        try {
            adminDataProvider()?.let { data ->
                val url = data.networkConfiguration.webSettings.endpoints.firstOrNull()?.url?:""
                view.loadWebUrl(url)
                scheduleLoadingDismiss()
            }
        } catch (e: Exception) {
            IntBorApp.showLog("H5 loading failed: ${e.message}")
        }
    }

    private fun scheduleLoadingDismiss() {
        view.showLoading()
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            view.hideLoading()
        }
    }

    override fun handleCloseAction() {
        TtPoint.postPointData(false, "closebrowser")
        view.closeActivity()
    }

    private fun logConfiguration(packageName: String) {
        IntBorApp.showLog("Package: $packageName")
    }
}
