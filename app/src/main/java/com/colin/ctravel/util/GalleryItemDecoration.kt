package com.colin.ctravel.util

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class GalleryItemDecoration(private val context: Context, private val left: Int, private val right: Int, private val top: Int, private val bottom: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = QMUIDisplayHelper.dp2px(context, top)
        outRect.bottom = QMUIDisplayHelper.dp2px(context, bottom)
        outRect.left = QMUIDisplayHelper.dp2px(context, left)
        outRect.right = QMUIDisplayHelper.dp2px(context, right)

    }
}
