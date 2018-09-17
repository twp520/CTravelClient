package com.colin.ctravel.view

import com.colin.ctravel.base.BaseView
import com.colin.ctravel.bean.PostInfo
import com.colin.ctravel.bean.User

/**
 *create by colin 2018/9/13
 */
interface MainView : BaseView {

    fun initUser(user: User)

    fun refreshList(data: MutableList<PostInfo>)

    fun refreshFail()
}