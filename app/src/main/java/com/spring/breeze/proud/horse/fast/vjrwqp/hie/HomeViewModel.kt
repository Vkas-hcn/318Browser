package com.spring.breeze.proud.horse.fast.vjrwqp.hie

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import com.spring.breeze.proud.horse.fast.cenklaj.cesa.BaseViewModel
import com.spring.breeze.proud.horse.fast.vjiropa.verv.DataUtils
import com.spring.breeze.proud.horse.fast.vjiropa.verv.TbasWebBean
import com.spring.breeze.proud.horse.fast.vjiropa.verv.WkvrnBean
import com.spring.breeze.proud.horse.fast.vjrwqp.vjir.CustomWebView

class HomeViewModel : BaseViewModel() {

    val historyListData: MutableLiveData<MutableList<WkvrnBean>> = MutableLiveData()
    val tabsListData: MutableLiveData<MutableList<TbasWebBean>> = MutableLiveData()
    val showPage: MutableLiveData<Int> = MutableLiveData(1)
    val showMenu: MutableLiveData<Boolean> = MutableLiveData(false)
    val showNoHistoryData: MutableLiveData<Boolean> = MutableLiveData(true)
    val showNoMarkData: MutableLiveData<Boolean> = MutableLiveData(true)
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun searchUrl(data: String): String {
        val urlPattern =
            "^(http://www\\.|https://www\\.|http://|https://)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(/.*)?$".toRegex()

        return if (urlPattern.matches(data)) {
            if (data.startsWith("http://") || data.startsWith("https://")) {
                data
            } else {
                "https://$data"
            }
        } else {
            ""
        }
    }



    fun closeKeyboard(view: View, context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showClearDialog(context: Context, deleteFun: () -> Unit) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Tip")
        builder.setMessage("Do you want to delete this history?")
        builder.setPositiveButton("Yes") { _, _ ->
            deleteFun()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun shareText(context: Context) {
        val text = "https://play.google.com/store/apps/details?id=${context.packageName}"
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun loadHistoryData(customWebView: CustomWebView) {
        val historyList = customWebView.getWebsiteInfoList()
        historyList.forEach { it.selected = false }
        historyListData.value = historyList
        showNoHistoryData.value = historyList.isEmpty()
    }

    fun loadTabsData(context: Context) {
        val tabsList = DataUtils.loadWebTabList(context) ?: mutableListOf()
        tabsListData.value = tabsList
    }

    fun deleteHistoryItem(context: Context, date: String,customWebView: CustomWebView) {
        showClearDialog(context) {
            customWebView.deleteWebsiteInfo( date)
            loadHistoryData(customWebView)
        }
    }

    fun deleteTabItem(context: Context, date: String) {
        DataUtils.removeWebTabById(context, date) {
            loadTabsData(context)
        }
    }

    fun addTab(context: Context, webPageTbas: TbasWebBean) {
        val webPageTbasList = DataUtils.loadWebTabList(context) ?: mutableListOf()
        webPageTbasList.add(0, webPageTbas)
        DataUtils.saveWebTabList(context, webPageTbasList)
        loadTabsData(context)
    }

    fun updateShowPage(page: Int) {
        showPage.value = page
    }

    fun updateShowMenu(visible: Boolean) {
        showMenu.value = visible
    }

    fun updateLoading(isLoading: Boolean) {
        this.isLoading.value = isLoading
    }
}
