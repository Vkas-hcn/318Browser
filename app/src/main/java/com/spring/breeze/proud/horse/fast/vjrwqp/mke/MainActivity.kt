package com.spring.breeze.proud.horse.fast.vjrwqp.mke

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.spring.breeze.proud.horse.fast.databinding.FragmentStartBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
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
                val intent = Intent(this, HomeAppActivity::class.java).apply {
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
    var job: Job? = null

    fun startRotation() {
        isRotationActive.value = true
        showOpenAd()
    }

    fun stopRotation() {
        isRotationActive.value = false
        liveHomeData.value = true
        job?.cancel()
        job = null
    }

    private fun showOpenAd() {
        job?.cancel()
        job = null
        job = lifecycleScope.launch {
            delay(2000)
            stopRotation()
        }
    }
}
