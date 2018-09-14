package com.colin.ctravel.photopicker

import android.app.Activity
import android.content.Intent
import android.support.annotation.ColorRes
import android.support.v4.app.Fragment

/**
 *create by colin 2018/9/14
 */
class ImagePicker {

    private var activity: Activity? = null
    private var fragment: Fragment? = null
    private var requestCode = -1
    private var colorRes: Int = -1
    private var maxCount: Int = 9

    constructor(activity: Activity, requestCode: Int, @ColorRes colorRes: Int, maxCount: Int) {
        this.activity = activity
        this.requestCode = requestCode
        this.colorRes = colorRes
        this.maxCount = maxCount
    }

    constructor(fragment: Fragment, requestCode: Int, @ColorRes colorRes: Int, maxCount: Int) {
        this.fragment = fragment
        this.requestCode = requestCode
        this.colorRes = colorRes
        this.maxCount = maxCount
    }

    fun start() {
        if (activity != null) {
            val intent = Intent(activity, ImagePickerAct::class.java)
            intent.putExtra("maxCount", this.maxCount)
            if (colorRes != -1) {
                intent.putExtra("color", colorRes)
            }
            activity?.startActivityForResult(intent, requestCode)
            return
        }

        if (fragment != null) {
            val intent = Intent(fragment?.activity, ImagePickerAct::class.java)
            intent.putExtra("maxCount", this.maxCount)
            if (colorRes != -1) {
                intent.putExtra("color", colorRes)
            }
            fragment?.startActivityForResult(intent, requestCode)
            return
        }

        throw NullPointerException("没有启动选项")
    }


    class Builder {
        private var activity: Activity? = null
        private var fragment: Fragment? = null
        private var requestCode = -1
        private var colorRes: Int = -1
        private var maxCount: Int = 9


        fun with(activity: Activity): Builder {
            this.activity = activity
            return this
        }

        fun with(fragment: Fragment): Builder {
            this.fragment = fragment
            return this
        }

        fun requestCode(requestCode: Int): Builder {
            this.requestCode = requestCode
            return this
        }

        fun maxCount(maxCount: Int): Builder {
            this.maxCount = maxCount
            return this
        }

        /*fun themColor(colorRes: Int): Builder {
            this.colorRes = colorRes
            return this
        }*/

        fun build(): ImagePicker {
            return if (activity != null) {
                ImagePicker(activity!!, requestCode, colorRes, maxCount)
            } else {
                ImagePicker(fragment!!, requestCode, colorRes, maxCount)
            }
        }
    }
}