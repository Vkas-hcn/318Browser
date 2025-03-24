package com.spring.breeze.proud.horse.fast.vjrwqp.vjir

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.spring.breeze.proud.horse.fast.vjiropa.verv.DataUtils
import com.spring.breeze.proud.horse.fast.vjiropa.verv.WkvrnBean

@SuppressLint("SetJavaScriptEnabled")
class CustomWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : WebView(context, attrs) {

    interface WebViewCallback {
        fun onPageStarted(url: String?)
        fun onPageFinished(url: String?)
        fun onProgressChanged(progress: Int)
    }

    private var callback: WebViewCallback? = null
    private var isHistory: Boolean = false
    private var webPageTitle: String = ""
    private var webPageIcon: Bitmap? = null
    private var webPageDate: String? = null

    init {
        webViewClient = CustomWebViewClient()
        webChromeClient = CustomWebChromeClient()
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
    }

    fun setOnWebViewCallbackListener(listener: WebViewCallback) {
        this.callback = listener
    }

    fun handleBackPress(): Boolean {
        return if (canGoBack()) {
            goBack()
            true
        } else {
            false
        }
    }

    fun saveWebsiteInfo() {
        val beanList = DataUtils.loadWebPageInfoList(context) ?: mutableListOf()
        beanList.add(WkvrnBean(webPageTitle, url ?: "", webPageDate ?: ""))
        DataUtils.saveWebPageInfoList(context, beanList)
    }

    fun getWebsiteInfoList(): MutableList<WkvrnBean> {
        return DataUtils.loadWebPageInfoList(context)?.sortedByDescending { it.date }?.toMutableList() ?: mutableListOf()
    }

    fun deleteWebsiteInfo(date: String) {
        val beanList = DataUtils.loadWebPageInfoList(context)?.apply {
            removeAll { it.date == date }
        }
        beanList?.let { DataUtils.saveWebPageInfoList(context, it) }
    }

    fun goForwardPage() {
        if (canGoForward()) goForward()
    }

    fun loadCustomWeb(url: String, isHistory: Boolean) {
        this.isHistory = isHistory
        clearCache(true)
        loadUrl(url)
    }
    fun getWeburl():String{
        return url ?: ""
    }

    fun refreshPage() {
        reload()
    }

    fun isAtEndOfForwardHistory(): Boolean = !canGoForward()
    fun isAtStartOfBackHistory(): Boolean = !canGoBack()



    private inner class CustomWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            url?.let {
                webPageDate = System.currentTimeMillis().toString()
                callback?.onPageStarted(url)
            }
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            callback?.onPageFinished(url)
            if (isHistory) {
                saveWebsiteInfo()
                isHistory = false
            }
        }
    }

    private inner class CustomWebChromeClient : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            webPageTitle = title ?: ""
        }

        override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
            super.onReceivedIcon(view, icon)
            webPageIcon = icon
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            callback?.onProgressChanged(newProgress)
        }
    }
}
