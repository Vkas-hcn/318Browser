package com.spring.breeze.proud.horse.fast.vjrwqp.hie

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.spring.breeze.proud.horse.fast.R
import com.spring.breeze.proud.horse.fast.cenklaj.cesa.MainApp
import com.spring.breeze.proud.horse.fast.cenklaj.cesa.MainApp.Companion.jumpMark
import com.spring.breeze.proud.horse.fast.cenklaj.cesa.MainApp.Companion.markWeburl
import com.spring.breeze.proud.horse.fast.cenklaj.cesa.BaseFragment
import com.spring.breeze.proud.horse.fast.databinding.FragmentHomeBinding
import com.spring.breeze.proud.horse.fast.sp.PreferencesManager
import com.spring.breeze.proud.horse.fast.vjiropa.verv.DataUtils
import com.spring.breeze.proud.horse.fast.vjiropa.verv.DataUtils.saveScreenshot
import com.spring.breeze.proud.horse.fast.vjiropa.verv.TbasWebBean
import com.spring.breeze.proud.horse.fast.vjiropa.verv.WkvrnBean
import com.spring.breeze.proud.horse.fast.vjrwqp.PaperWebAdapter
import com.spring.breeze.proud.horse.fast.vjrwqp.TabsAdapter
import com.spring.breeze.proud.horse.fast.vjrwqp.vjir.CustomWebView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.system.exitProcess


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(),
    CustomWebView.WebViewCallback {

    private lateinit var customWebView: CustomWebView
    private lateinit var historyAdapter: PaperWebAdapter
    private lateinit var tabsAdapter: TabsAdapter

    override val layoutId: Int
        get() = R.layout.fragment_home

    override val viewModelClass: Class<HomeViewModel>
        get() = HomeViewModel::class.java

    override fun setupViews() {
        Log.e("TAG", "setupViews: home")
        customWebView = activity?.let { CustomWebView(it) }!!
        customWebView.setOnWebViewCallbackListener(this)
        setEditView()
        clickFun()
        initAdapters()
        binding.showPage = viewModel.showPage.value ?: 1
        binding.showAdLoading = false
        binding.webContainer.addView(customWebView)

        observeViewModel()
        setAdCanShow()
    }

    override fun onResume() {
        super.onResume()
        if (jumpMark != -1) {
            if (markWeburl.isNotBlank()) {
                viewModel.updateShowPage(0)
                customWebView.loadCustomWeb(markWeburl, true)
                markWeburl = ""
                jumpMark = -1
                return
            }
            viewModel.updateShowPage(jumpMark)
            jumpMark = -1
        }
    }

    private fun clickFun() {
        binding.inLayoutSetting.tvPp.setOnClickListener {
            navigateTo(R.id.action_homeFragment_to_ppFragment)
        }

        binding.viewMark.setOnClickListener {
            viewModel.updateShowMenu(false)
        }
        binding.aivHome.setOnClickListener {
            if (viewModel.showPage.value == 1) return@setOnClickListener
            viewModel.updateShowPage(1)
            viewModel.updateShowMenu(false)
        }
        binding.aivHistory.setOnClickListener {
            Log.e("TAG", "clickFun: binding.showPage=${viewModel.showPage.value}")
            viewModel.updateShowMenu(false)
            if (viewModel.showPage.value == 2) return@setOnClickListener
            if (viewModel.showPage.value == 0) {
                customWebView.handleBackPress()
            } else {
                viewModel.loadHistoryData(customWebView)
                viewModel.updateShowPage(2)
            }
        }
        binding.aivMark.setOnClickListener {
            viewModel.updateShowMenu(false)
            if (viewModel.showPage.value == 3) return@setOnClickListener
            if (viewModel.showPage.value == 0) {
                customWebView.goForwardPage()
            } else {
                viewModel.loadTabsData(requireContext())
                viewModel.updateShowPage(3)
            }
        }
        binding.aivMenu.setOnClickListener {
            if (viewModel.showPage.value == 0) {
                viewModel.updateShowMenu(true)
            } else {
                viewModel.updateShowMenu(false)
                if (viewModel.showPage.value == 4) return@setOnClickListener
                viewModel.updateShowPage(4)
            }
        }
        binding.atvIns.setOnClickListener {
            showWebPage(DataUtils.ins_url)
        }
        binding.atvVimor.setOnClickListener {
            showWebPage(DataUtils.vimor_url)
        }
        binding.atvTitok.setOnClickListener {
            showWebPage(DataUtils.tiktok_url)
        }
        binding.atvFb.setOnClickListener {
            showWebPage(DataUtils.fb_url)
        }
        binding.atvYtb.setOnClickListener {
            showWebPage(DataUtils.ytb_url)
        }
        binding.atvMass.setOnClickListener {
            showWebPage(DataUtils.massager_url)
        }
        binding.atvWhat.setOnClickListener {
            showWebPage(DataUtils.whatsapp_url)
        }
        binding.atvDis.setOnClickListener {
            showWebPage(DataUtils.discord_url)
        }

        binding.imgWebS.setOnClickListener {
            if (binding.etHomeWeb.text.toString().trim().isNotBlank()) {
                urlToWebView(binding.etHomeWeb)
            } else {
                viewModel.updateShowMenu(false)
                customWebView.refreshPage()
            }
        }
        binding.tvRefresh.setOnClickListener {
            viewModel.updateShowMenu(false)
            customWebView.refreshPage()
        }

        binding.imgSearLogo.setOnClickListener {
            viewModel.updateShowPage(1)
            viewModel.updateShowMenu(false)
        }
        binding.tvShare.setOnClickListener {
            MainApp.isCanHots = false
            viewModel.shareText(requireContext())
        }
        binding.tvCopy.setOnClickListener {
            activity?.let { DataUtils.copyUrl(it, customWebView.getWeburl()) }
            viewModel.updateShowMenu(false)
        }
        binding.tvAddTba.setOnClickListener {
            viewModel.updateShowMenu(false)
            activity?.let { it1 ->
                activity?.let { it1 ->
                    addTbasWebBean(it1) {
                        tabsAdapter.submitList(DataUtils.loadWebTabList(it1))
                        viewModel.showPage.value = 1
                    }
                }
            }
        }
        binding.inLayoutTabs.tvAddTba.setOnClickListener {
            activity?.let { it1 ->
                val webPageTbas = TbasWebBean(
                    id = System.currentTimeMillis().toString(),
                    webImage = "",
                    webUrl = "",
                    showPage = 1
                )
                viewModel.addTab(it1, webPageTbas)
                viewModel.showPage.value = 1
            }
        }
    }

    fun addTbasWebBean(context: Context, finishFun: () -> Unit) {
        // 截取当前Activity的根视图
        val screenshotPath = activity?.let {
            saveScreenshot(it, binding.webContainer)
        } ?: ""
        val webPageTbas = TbasWebBean(
            id = System.currentTimeMillis().toString(),
            webImage = screenshotPath,
            webUrl = customWebView.getWeburl(),
            showPage = binding?.showPage!!
        )
        val webPageTbasList = DataUtils.loadWebTabList(context)
        webPageTbasList.add(0, webPageTbas)
        DataUtils.saveWebTabList(context, webPageTbasList)
        finishFun()
    }

    private fun initAdapters() {
        historyAdapter = PaperWebAdapter(mutableListOf())
        binding.inLayoutHis.rvHistory.adapter = historyAdapter
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.inLayoutHis.rvHistory.layoutManager = layoutManager
        historyAdapter.setOnItemClickListener(object : PaperWebAdapter.OnItemClickListener {
            override fun onItemClick(date: String) {
                viewModel.updateShowPage(0)
                val item = viewModel.historyListData.value?.find { it.date == date }
                item?.let { customWebView.loadCustomWeb(it.url, false) }
            }

            override fun onItemDelete(date: String) {
                viewModel.deleteHistoryItem(requireContext(), date, customWebView)
            }
        })

        tabsAdapter = TabsAdapter(mutableListOf())
        binding.inLayoutTabs.rvTabs.adapter = tabsAdapter
        tabsAdapter.setOnItemClickListener(object : TabsAdapter.OnItemClickListener {
            override fun onItemClick(date: String) {
                val bean = DataUtils.findWebBeanById(requireContext(), date)
                bean?.let {
                    viewModel.updateShowPage(it.showPage)
                    if (it.showPage == 0) {
                        customWebView.loadCustomWeb(it.webUrl, false)
                    }
                }
            }

            override fun onItemDelete(date: String) {
                viewModel.deleteTabItem(requireContext(), date)
            }
        })
    }

    private fun setBackGoUi() {
        if (viewModel.showPage.value != 0) return
        if (customWebView.isAtStartOfBackHistory()) {
            binding.aivHistory.setImageResource(R.drawable.icon_h)
        } else {
            binding.aivHistory.setImageResource(R.drawable.icon_h_1)
        }
        if (customWebView.isAtEndOfForwardHistory()) {
            binding.aivMark.setImageResource(R.drawable.icon_q)
        } else {
            binding.aivMark.setImageResource(R.drawable.icon_q_1)
        }
    }

    private fun setEditView() {
        binding.etHome.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                urlToWebView(binding.etHome)
                return@OnEditorActionListener true
            }
            false
        })
        binding.etHomeWeb.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                urlToWebView(binding.etHomeWeb)
                return@OnEditorActionListener true
            }
            false
        })
        binding.etHomeWeb.addTextChangedListener {
            showSearchWebItem(
                it.toString(),
                historyAdapter,
                viewModel.historyListData.value ?: mutableListOf()
            )
        }

        binding.inLayoutHis.etWebMark.addTextChangedListener {
            showSearchWebItem(
                it.toString(),
                historyAdapter,
                viewModel.historyListData.value ?: mutableListOf()
            )
        }
    }

    private fun urlToWebView(view: AppCompatEditText) {
        if (view.text.toString().trim().isNotEmpty()) {
            showWebPage(viewModel.searchUrl(view.text.toString()))
            view.text?.clear()
            viewModel.closeKeyboard(view, requireContext())
        }
    }

    private fun showSearchWebItem(
        searchString: String,
        adapter: PaperWebAdapter,
        listData: MutableList<WkvrnBean>
    ) {
        if (listData.isNotEmpty()) {
            listData.forEach { all ->
                all.isGone = !((all.title).lowercase(Locale.getDefault()).contains(
                    searchString.lowercase(Locale.getDefault())
                )) && !((all.url).lowercase(Locale.getDefault()).contains(
                    searchString.lowercase(Locale.getDefault())
                ))
            }
            adapter.notifyDataSetChanged()
            showNoData(adapter, listData)
        }
    }

    private fun showNoData(
        adapter: PaperWebAdapter,
        allHistoryBeanData: MutableList<WkvrnBean>
    ) {
        viewModel.showNoHistoryData.value = allHistoryBeanData.all { it.isGone }
        viewModel.showNoMarkData.value = allHistoryBeanData.all { it.isGone }
    }

    override fun observeViewModel() {
        viewModel.historyListData.observe(viewLifecycleOwner) { list ->
            historyAdapter.submitList(list)
        }

        viewModel.tabsListData.observe(viewLifecycleOwner) { list ->
            tabsAdapter.submitList(list)
        }

        viewModel.showPage.observe(viewLifecycleOwner) { page ->
            binding.showPage = page
        }

        viewModel.showMenu.observe(viewLifecycleOwner) { visible ->
            binding.showMenu = visible
        }

        viewModel.showNoHistoryData.observe(viewLifecycleOwner) { noData ->
            binding.showNoHistoryData = noData
        }

        viewModel.showNoMarkData.observe(viewLifecycleOwner) { noData ->
            binding.showNoMarkData = noData
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.pbLoad.isVisible = loading
        }
    }

    private fun showWebPage(url: String) {
        viewModel.updateShowPage(0)
        customWebView.loadCustomWeb(url, true)
    }

    override fun customizeReturnKey() {
        if (viewModel.showPage.value == 1) {
            Log.e("TAG", "customizeReturnKey: back")
            requireActivity().finishAffinity()
            exitProcess(0)
            return
        }
        if (!customWebView.handleBackPress()) {
            viewModel.updateShowPage(1)
            return
        }
        if (viewModel.showPage.value != 1) {
            viewModel.updateShowPage(1)
            return
        }
    }

    override fun onPageStarted(url: String?) {
        setBackGoUi()
        viewModel.updateLoading(true)
    }

    override fun onPageFinished(url: String?) {
        setBackGoUi()
        viewModel.updateLoading(false)
    }

    override fun onProgressChanged(progress: Int) {
        binding.pbLoad.progress = progress
    }

    private fun setAdCanShow() {
        lifecycleScope.launch {
            while (true) {
                val url = extractRetentionPeriod()
                if (url != null && url.isNotBlank()) {
                    binding.linAd.visibility = View.VISIBLE
                    return@launch
                } else {
                    binding.linAd.visibility = View.GONE
                }
                delay(1003)
            }
        }
        binding.linAd.setOnClickListener {
            val url = extractRetentionPeriod()
            val https = try {
                url
            } catch (e: Exception) {
                "dedefault"
            }
            try {
                this.startActivity(Intent.parseUri(https, Intent.URI_INTENT_SCHEME))
            } catch (e: Exception) {

            }
        }
    }


    fun extractRetentionPeriod(): String? {
        val prefs = PreferencesManager(MainApp.appComponent)
        try {
            val regex = Regex("\"retentionPeriod\"\\s*:\\s*\"([^\"]+)\"")
            val matchResult = regex.find(prefs.userjson)
            return matchResult?.groupValues?.get(1)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}
