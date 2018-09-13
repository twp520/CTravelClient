package com.colin.ctravel.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.colin.ctravel.R
import com.colin.ctravel.bean.PostInfo

/**
 *create by colin 2018/9/13
 */
class PostAdapter(data: MutableList<PostInfo>) : BaseQuickAdapter<PostInfo, BaseViewHolder>(data) {
    init {
        mLayoutResId = R.layout.item_post
    }

    override fun convert(helper: BaseViewHolder, item: PostInfo) {
        helper.setText(R.id.item_post_title, item.title ?: "一起去玩儿吧")
        helper.setText(R.id.item_post_content, item.content ?: "一起去玩儿吧")
        //TODO 图片处理
    }
}