package com.colin.ctravel.base

import android.content.Context

/**
 *create by colin
 */
interface BaseView {

    //初始化view
    fun initView()

    /**
     * 显示一个loading
     *
     */
    fun showLoading()

    /**
     * 关闭loading
     */
    fun dismissLoading()

    /**
     * 弹一个吐司提示
     *
     *
     * * @param msg  提示信息
     */
    fun showTipMessage(msg: String)


    /**
     * 获得上下文对象
     *
     * @return 上下文对象
     */
    fun getViewContext(): Context

    /**
     * 管理 网络请求生命周期的 key
     *
     * @return key
     */
    fun getNetKey(): String


    fun showNetErrorMsg(throwable: Throwable)
}