package com.colin.ctravel.widget

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import com.colin.ctravel.activity.PostDetailAct

class MyViewPager(context: Context, attr: AttributeSet?) : ViewPager(context, attr) {

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)
        requestLayout()
        postInvalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var hms = heightMeasureSpec

        var height = 0
        for (i in 0 until (if (adapter == null) childCount else {
            (adapter as PostDetailAct.MyPageAdapter).count
        })) {

            val child = if (adapter != null) {
                (adapter as PostDetailAct.MyPageAdapter).getItemView(i)
            } else {
                getChildAt(i)
            }
            child?.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            val h = child?.measuredHeight ?: 0
            if (h > height) height = h
        }
        hms = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, hms)
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @param view the base view with already measured height
     *
     * @return The height of the view, honoring constraints from measureSpec
     */
    private fun measureHeight(measureSpec: Int, view: View?): Int {
        var result = 0
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            // set the height from the base view if available
            if (view != null) {
                result = view.measuredHeight
            }
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }
}