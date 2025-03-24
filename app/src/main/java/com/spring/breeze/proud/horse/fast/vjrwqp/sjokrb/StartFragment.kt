package com.spring.breeze.proud.horse.fast.vjrwqp.sjokrb

import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import com.spring.breeze.proud.horse.fast.R
import com.spring.breeze.proud.horse.fast.cenklaj.cesa.BaseFragment
import com.spring.breeze.proud.horse.fast.databinding.FragmentStartBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StartFragment : BaseFragment<FragmentStartBinding, StartViewModel>() {
    override val layoutId: Int
        get() = R.layout.fragment_start

    override val viewModelClass: Class<StartViewModel>
        get() = StartViewModel::class.java

    override fun setupViews() {
        // No need to call postProgress here
    }

    override fun observeViewModel() {
        viewModel.isRotationActive.observe(viewLifecycleOwner) { isActive ->
            if (isActive) {
                rotateImage(binding.imgLoading)
            } else {
                stopRotation(binding.imgLoading)
            }
        }

        viewModel.liveHomeData.observe(viewLifecycleOwner) {
            if (it) {
                navigateTo(R.id.action_startFragment_to_homeFragment)
            }
        }
    }

    override fun customizeReturnKey() {
        // Custom return key logic
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
}
