package com.reet.prep.academy

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.viewpager.widget.ViewPager

class VerticalViewPager : ViewPager {

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }

    private fun initialize() {
        setPageTransformer(true, VerticalPageTransformer())
        overScrollMode = OVER_SCROLL_NEVER
    }

    private inner class VerticalPageTransformer : ViewPager.PageTransformer {

        override fun transformPage(page: View, position: Float) {
            // now check positions
            when {
                position < -1 -> { // [-infinite, -1] ; if this page is way off screen to the left
                    page.alpha = 0f
                }
                position <= 0 -> { // [-1, 0] ; use the default slide transition when moving to the left
                    page.alpha = 1f
                    // counteract the default slide transition
                    page.translationX = page.width * -position
                    // set Y position to swipe in from top
                    val yPosition = position * page.height
                    page.translationY = yPosition
                    page.scaleX = 1f
                    page.scaleY = 1f
                }
                position <= 1 -> { // [0, 1] ; counteract the default slide transition
                    page.translationX = page.width * -position
                    // to scale the page down
                    val scale = 0.75f + (1 - 0.75f) * (1 - Math.abs(position))
                    page.scaleX = scale
                    page.scaleY = scale
                }
                else -> { // [1, +infinity] ; this page is way off screen to the right
                    page.alpha = 0f
                }
            }
        }
    }

    private fun swapXYCoordinates(event: MotionEvent): MotionEvent {
        // now swap x and y coordinates using this
        val width = width.toFloat()
        val height = height.toFloat()
        val newX = (event.y / height) * width
        val newY = (event.x / width) * height
        event.setLocation(newX, newY)
        return event
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val intercepted = super.onInterceptTouchEvent(swapXYCoordinates(ev))
        swapXYCoordinates(ev)
        return intercepted
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return super.onTouchEvent(swapXYCoordinates(ev))
    }
}
