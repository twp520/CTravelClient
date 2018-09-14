package com.colin.ctravel.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GalleryItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private int left, right, top, bottom;

    public GalleryItemDecoration(Context context, int left, int right, int top, int bottom) {
        this.context = context;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = QMUIDisplayHelper.dp2px(context, top);
        outRect.bottom = QMUIDisplayHelper.dp2px(context, bottom);
        outRect.left = QMUIDisplayHelper.dp2px(context, left);
        outRect.right = QMUIDisplayHelper.dp2px(context, right);

    }
}
