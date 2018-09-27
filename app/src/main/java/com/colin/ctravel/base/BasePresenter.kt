package com.colin.ctravel.base

import android.os.Bundle

/**
 *create by colin 2018/9/12
 */
interface BasePresenter {

    fun onAttach(bundle: Bundle)

    fun onDettach()
}