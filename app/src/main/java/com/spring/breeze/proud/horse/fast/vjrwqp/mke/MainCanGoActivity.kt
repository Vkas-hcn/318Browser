package com.spring.breeze.proud.horse.fast.vjrwqp.mke

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashAdExtraInfo
import com.anythink.splashad.api.ATSplashAdListener
import com.during.festival.rain.falls.one.utils.PngAllData
import com.spring.breeze.proud.horse.fast.cenklaj.cesa.MainApp
import com.spring.breeze.proud.horse.fast.databinding.FragmentStartBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class MainCanGoActivity : AppCompatActivity() {
    private val binding by lazy { FragmentStartBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        startRotation()
        observeViewModel()
        onBackPressedDispatcher.addCallback {
        }
    }

     fun observeViewModel() {
       isRotationActive.observe(this) { isActive ->
            if (isActive) {
                rotateImage(binding.imgLoading)
            } else {
                stopRotation(binding.imgLoading)
            }
        }

      liveHomeData.observe(this) {
            if (it) {
                val intent = Intent(this, MainAppActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun rotateImage(imageView: ImageView) {
        val animator = ObjectAnimator.ofFloat(imageView, View.ROTATION, 0f, 360f).apply {
            this.duration = 900
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
        }
        animator.start()
        imageView.tag = animator
    }

    private fun stopRotation(imageView: ImageView) {
        val animator = imageView.tag as? ObjectAnimator
        animator?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cleanup if necessary
    }
    val liveHomeData = MutableLiveData<Boolean>()
    val isRotationActive = MutableLiveData<Boolean>()
    private lateinit var splashAd: ATSplashAd
    var job: Job? = null

    fun startRotation() {
            isRotationActive.value = true
            initSpAd( )

    }
    fun stopRotation() {
        isRotationActive.value = false
        liveHomeData.value = true
        job?.cancel()
        job = null
    }

    private fun initSpAd() {
        // 创建开屏广告对象
        splashAd = ATSplashAd(
            MainApp.application,
            PngAllData.getConfig().openid,
            object : ATSplashAdListener {

                override fun onAdLoaded(p0: Boolean) {
                    Log.e("TAG", "open ad load success", )
                }

                override fun onAdLoadTimeout() {
                    Log.e("TAG", "open ad load onAdLoadTimeout", )

                }

                override fun onNoAdError(p0: AdError?) {
                    Log.e("TAG", "open ad load onNoAdError", )

                }

                override fun onAdShow(p0: ATAdInfo?) {
                    Log.e("TAG", "open ad load onAdShow", )

                }

                override fun onAdClick(p0: ATAdInfo?) {
                }

                override fun onAdDismiss(p0: ATAdInfo?, p1: ATSplashAdExtraInfo?) {
                    Log.e("TAG", "open ad load onAdDismiss", )
                    stopRotation()
                }
            })
        splashAd.loadAd()
        showOpenAd()
    }

    private fun showOpenAd() {
        job?.cancel()
        job = null
        job = lifecycleScope.launch {
            try {
                withTimeout(10000L) {
                    while (isActive) {
                        if (splashAd.isAdReady) {
                            splashAd.show(this@MainCanGoActivity, binding.startAll)
                            break
                        }
                        delay(500L)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                stopRotation()
            }
        }
    }
}
