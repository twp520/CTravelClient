package com.colin.ctravel.widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.colin.ctravel.R


class MyLoadingDialog : DialogFragment() {

    private var mPlane: ImageView? = null
    private var mCloud1: ImageView? = null
    private var mCloud2: ImageView? = null
    private var mCloud3: ImageView? = null
    private var mSet: AnimatorSet? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_loading, container, false)
        mPlane = view.findViewById(R.id.dialog_loading_plane)
        mCloud1 = view.findViewById(R.id.dialog_loading_cloud1)
        mCloud2 = view.findViewById(R.id.dialog_loading_cloud2)
        mCloud3 = view.findViewById(R.id.dialog_loading_cloud3)
        return view
    }

    override fun onStart() {
        //TODO 开始动画
        val win = dialog.window
        val params = win.attributes
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = 500
        params.height = 300
        win.attributes = params

        mPlane?.rotation = -15f
        mPlane?.translationX
        if (mCloud1 != null && mCloud2 != null && mCloud3 != null) {
            val animator1 = ObjectAnimator.ofFloat(mCloud1!!, "translationX", 100f, -600f)
            animator1.repeatCount = -1
            val animator2 = ObjectAnimator.ofFloat(mCloud2!!, "translationX", 100f, -600f)
            animator2.repeatCount = -1
            animator2.startDelay = 300
            val animator3 = ObjectAnimator.ofFloat(mCloud3!!, "translationX", 100f, -600f)
            animator3.repeatCount = -1
            animator3.startDelay = 500
            mSet = AnimatorSet()
            mSet?.duration = 1500
            mSet?.play(animator1)
                    ?.with(animator2)
                    ?.with(animator3)
            mSet?.start()
        }
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        mSet?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        //TODO 停止动画
        mSet?.pause()
    }

}