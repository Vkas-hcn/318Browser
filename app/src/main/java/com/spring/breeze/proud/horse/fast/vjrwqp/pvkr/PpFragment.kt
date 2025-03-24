package com.spring.breeze.proud.horse.fast.vjrwqp.pvkr

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.spring.breeze.proud.horse.fast.R
import com.spring.breeze.proud.horse.fast.cenklaj.cesa.BaseFragment
import com.spring.breeze.proud.horse.fast.cenklaj.cesa.BaseViewModel
import com.spring.breeze.proud.horse.fast.databinding.FragmentPpBinding
import com.spring.breeze.proud.horse.fast.vjiropa.verv.DataUtils

class PpFragment : BaseFragment<FragmentPpBinding, BaseViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_pp

    override val viewModelClass: Class<BaseViewModel>
        get() = BaseViewModel::class.java


    override fun setupViews() {
        binding.iconBack.setOnClickListener {
            customizeReturnKey()
        }
        binding.webPp.apply {
            binding.showPaPageLoading = true
            loadUrl(DataUtils.pp_url)
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.showPaPageLoading = false
                }
            }
        }
    }

    override fun observeViewModel() {
    }

    override fun customizeReturnKey() {
        if (binding?.showPaPageLoading!!) {
            binding.showPaPageLoading = false
            return
        }
        navigateTo(R.id.action_ppFragment_to_homeFragment)
    }

}