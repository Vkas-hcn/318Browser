package com.spring.breeze.proud.horse.fast.vjrwqp

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spring.breeze.proud.horse.fast.R
import kotlin.math.abs

class CardCarouselView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val screenWidth by lazy { Resources.getSystem().displayMetrics.widthPixels }
    private val itemWidth by lazy { (screenWidth * 0.8).toInt() }
    private val peekWidth by lazy { (screenWidth * 0.1).toInt() }

    init {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        addItemDecoration(CarouselItemDecoration())
        addOnScrollListener(CarouselScrollListener())
        attachSnapHelper()
        setPadding(peekWidth, 0, peekWidth, 0)
        clipToPadding = false
    }

    private fun attachSnapHelper() {
        val snapHelper = object : LinearSnapHelper() {
            override fun findTargetSnapPosition(
                layoutManager: LayoutManager,
                velocityX: Int,
                velocityY: Int
            ): Int {
                val centerView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
                val position = getChildAdapterPosition(centerView)
                if (position == RecyclerView.NO_POSITION) return RecyclerView.NO_POSITION

                // 确保滑动到最近位置
                val delta = if (velocityX > 0) 1 else -1
                return (position + delta).coerceIn(0, (adapter?.itemCount ?: 1) - 1)
            }
        }
        snapHelper.attachToRecyclerView(this)
    }

    inner class CarouselScrollListener : OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            updateItemTransformations()
        }

        private fun updateItemTransformations() {
            val centerX = width / 2f
            for (i in 0 until childCount) {
                val child = getChildAt(i) ?: continue
                val childCenterX = (child.left + child.right) / 2f
                val distanceFromCenter = abs(centerX - childCenterX)

                // 计算缩放比例和位移
                val scale = 1f - 0.2f * (distanceFromCenter / centerX)
                val translationX = if (childCenterX < centerX) {
                    -0.2f * (centerX - childCenterX)
                } else {
                    0.2f * (childCenterX - centerX)
                }

                child.scaleX = scale
                child.scaleY = scale
                child.translationX = translationX
            }
        }
    }

    inner class CarouselItemDecoration : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: State
        ) {
            outRect.left = (screenWidth - itemWidth) / 2
            outRect.right = (screenWidth - itemWidth) / 2
        }
    }

    // 适配器示例
    class CardAdapter : ListAdapter<Any, CardViewHolder>(DIFF_CALLBACK) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_card, parent, false)
            view.layoutParams.width = (parent.width * 0.8).toInt()
            return CardViewHolder(view)
        }

        override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
            // 绑定数据
        }

        companion object {
            private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Any>() {
                override fun areItemsTheSame(oldItem: Any, newItem: Any) = oldItem == newItem
                override fun areContentsTheSame(oldItem: Any, newItem: Any) = true
            }
        }
    }

    class CardViewHolder(view: View) : ViewHolder(view)
}