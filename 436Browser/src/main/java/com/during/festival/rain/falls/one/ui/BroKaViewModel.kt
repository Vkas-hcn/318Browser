package com.during.festival.rain.falls.one.ui

import android.content.Context
import androidx.lifecycle.*
import com.during.festival.rain.falls.one.main.IntBorApp
import com.during.festival.rain.falls.one.main.IntBorApp.okSpBean
import com.during.festival.rain.falls.one.utils.AppPointData
import com.during.festival.rain.falls.one.utils.TtPoint
import kotlinx.coroutines.*
import kotlin.random.Random

class BroKaViewModel : ViewModel() {

    private val _adReady = MutableLiveData<Boolean>()
    val adReady: LiveData<Boolean> get() = _adReady

    private val _navigateToH5 = MutableLiveData<Boolean>()
    val navigateToH5: LiveData<Boolean> get() = _navigateToH5

    private val _showAd = MutableLiveData<Long>()
    val showAd: LiveData<Long> get() = _showAd

    private val _adSuccessTrack = MutableLiveData<Unit>()
    val adSuccessTrack: LiveData<Unit> get() = _adSuccessTrack

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        initializeState()
    }

    private fun initializeState() {
        // 初始化状态逻辑
        _adReady.value = IntBorApp.adManager.interstitialAd.isAdReady
    }

    fun determineContentType(context: Context) {
      okSpBean.isAdFailCount = 0
//        val isH5State = PngCanGo.isH5State
//        _navigateToH5.value = isH5State
//        if (!isH5State) {
            AppPointData.firstExternalBombPoint()
            viewModelScope.launch {
                val delayDuration = generateRandomDelay()
                TtPoint.postPointData(false, "starup", "time", delayDuration / 1000)
                _showAd.value = delayDuration
            }
//        }
    }

    private fun generateRandomDelay(): Long {
        val adminData = IntBorApp.getAdminData()
        return adminData?.assetManagement?.let { it ->
            val minDelay = it.delayConfiguration.minDelay.toLong() ?: 0L
            val maxDelay = it.delayConfiguration.maxDelay.toLong() ?: 0L
            Random.nextLong(minDelay, maxDelay + 1)
        } ?: run {
            Random.nextLong(2000, 3000 + 1)
        }
    }

    fun handleAdShow(delayDuration: Long) {
        viewModelScope.launch {
            TtPoint.postPointData(false, "delaytime", "time", delayDuration / 1000)
            IntBorApp.showAdTime = System.currentTimeMillis()
            _adSuccessTrack.value = Unit
        }
    }

    fun trackAdSuccess() {
        viewModelScope.launch {
            delay(30000)
            if (IntBorApp.showAdTime > 0) {
                TtPoint.postPointData(false, "show", "t", "30")
                IntBorApp.showAdTime = 0
            }
        }
    }

    override fun onCleared() {
        viewModelJob.cancel()
        super.onCleared()
    }
}
