package com.colin.ctravel.presenter

import com.colin.ctravel.base.BasePresenter

/**
 *create by colin 2018/9/27
 */
interface PostDetailPresenter : BasePresenter {

    fun loadCommentData()

    fun favoritePost()

    fun sendComment(content:String)
}