package com.colin.ctravel.presenter

import com.colin.ctravel.base.BasePresenter

/**
 *create by colin 2018/9/13
 */
interface MainPresenter : BasePresenter {

    fun init()

    /**
     * 获取数据
     * @param page 页码
     */
    fun loadData(page: Int)
}