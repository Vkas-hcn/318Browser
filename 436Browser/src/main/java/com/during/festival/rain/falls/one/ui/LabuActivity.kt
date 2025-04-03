package com.during.festival.rain.falls.one.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.during.festival.rain.falls.one.R
import com.during.festival.rain.falls.one.utils.TtPoint
import com.during.festival.rain.falls.one.main.IntBorApp
import com.during.festival.rain.falls.one.appbean.BrowserAllBean
import com.during.festival.rain.falls.one.utils.PngAllData
import com.facebook.appevents.codeless.internal.ViewHierarchy.setOnClickListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LabuActivity : AppCompatActivity(), LabuContract.View {
    // View Components
    private lateinit var webView: WebView
    private lateinit var loadingLayout: LinearLayout
    private lateinit var closeButton: ImageView

    // Presenter
    private lateinit var presenter: LabuContract.Presenter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeViews()
        setupPresenter()
        IntBorApp.h5Limiter.recordAdShown()
        presenter.initWebConfiguration()
        presenter.loadH5Content()
    }

    private fun initializeViews() {
        setContentView(R.layout.activity_net)
        webView = findViewById(R.id.web_net)
        loadingLayout = findViewById(R.id.ll_loading)
        closeButton = findViewById(R.id.iv_close)
        closeButton.setOnClickListener { presenter.handleCloseAction() }
    }

    private fun setupPresenter() {
        presenter = LabuPresenter(
            view = this,
            adminDataProvider = { IntBorApp.getAdminData() }
        )
    }

    // region View Contract Implementation
    override fun configureWebSettings(packageName: String) {
        webView.settings.apply {
            userAgentString = "$userAgentString/$packageName"
            javaScriptEnabled = true
        }
    }

    override fun setupWebClient(client: WebViewClient) {
        webView.webViewClient = client
    }

    override fun loadWebUrl(url: String) {
        webView.loadUrl(url)
    }

    override fun showLoading() {
        loadingLayout.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingLayout.visibility = View.GONE
    }

    override fun closeActivity() {
        finish()
    }
    // endregion
}