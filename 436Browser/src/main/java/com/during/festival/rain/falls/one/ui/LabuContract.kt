package com.during.festival.rain.falls.one.ui

import android.webkit.WebViewClient

interface LabuContract {
    interface View {
        fun configureWebSettings(packageName: String)
        fun setupWebClient(client: WebViewClient)
        fun loadWebUrl(url: String)
        fun showLoading()
        fun hideLoading()
        fun closeActivity()
    }

    interface Presenter {
        fun initWebConfiguration()
        fun handleCloseAction()
        fun loadH5Content()
    }
}