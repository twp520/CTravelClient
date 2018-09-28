package com.colin.ctravel.view

import android.arch.lifecycle.Lifecycle
import com.colin.ctravel.base.BaseView
import com.colin.picklib.Image

/**
 *create by colin 2018/9/14
 */
interface SendPostView : BaseView {

    fun getTitleText(): String
    fun getDesText(): String
    fun getDepText(): String
    fun getDateText(): String
    fun getContentText(): String
    fun getPhotos(): MutableList<Image>

    fun getLifecycle(): Lifecycle

    fun sendSuccess()
}