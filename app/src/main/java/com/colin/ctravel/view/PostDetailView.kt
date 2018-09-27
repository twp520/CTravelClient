package com.colin.ctravel.view

import com.colin.ctravel.base.BaseView
import com.colin.ctravel.bean.Comment

/**
 *create by colin 2018/9/27
 */
interface PostDetailView : BaseView {

    fun favSuccess()

    fun setCommentData(commentList: MutableList<Comment>?)

    fun commentSuccess(comment:Comment)

    fun commentFail()
}