package com.colin.ctravel.presenter

import com.colin.ctravel.base.BasePresenter

/**
 *create by colin 2018/9/12
 */
interface LoginPresenter : BasePresenter {

    fun login(account: String, pwd: String)
}