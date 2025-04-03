package com.during.festival.rain.falls.one.ui

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.during.festival.rain.falls.one.utils.TtPoint
import com.during.festival.rain.falls.one.main.IntBorApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BroKaActivity : AppCompatActivity() {

    private val viewModel: BroKaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IntBorApp.showLog("BroKaActivity onCreate")
        initializeViewModel()
        observeViewModel()
    }

    private fun initializeViewModel() {
        viewModel.determineContentType(this)
    }

    private fun observeViewModel() {
//        viewModel.navigateToH5.observe(this) { isH5 ->
//            if (isH5) {
//                navigateToH5Activity()
//            }
//        }

        viewModel.showAd.observe(this) { delayDuration ->
            lifecycleScope.launch {
                if (delayDuration != null) {
                    handleAdDisplay(delayDuration)
                }
            }

        }

        viewModel.adSuccessTrack.observe(this) {
            viewModel.trackAdSuccess()
        }

        viewModel.adReady.observe(this) { isReady ->
            if (!isReady) {
                finish()
            }
        }
    }

//    private fun navigateToH5Activity() {
//        Intent(this, LabuActivity::class.java).apply {
//            startActivity(this)
//            TtPoint.postPointData(false, "browserjump")
//        }
//    }

    private suspend fun handleAdDisplay(delayDuration: Long) {
        delay(delayDuration)
        if(IntBorApp.adManager.interstitialAd.isAdReady){
            TtPoint.postPointData(false, "isready")
            IntBorApp.adManager.interstitialAd.show(this)
        }
        viewModel.handleAdShow(delayDuration)
    }

    override fun onDestroy() {
        (window.decorView as? ViewGroup)?.removeAllViews()
        super.onDestroy()
    }
}
