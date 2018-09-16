package com.colin.ctravel.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.colin.ctravel.R
import com.colin.ctravel.bean.PostInfo
import com.colin.ctravel.util.GlideApp
import org.json.JSONArray

/**
 *create by colin 2018/9/13
 */
class PostAdapter(data: MutableList<PostInfo>) : BaseQuickAdapter<PostInfo, BaseViewHolder>(data) {
    init {
        mLayoutResId = R.layout.item_post
    }

    override fun convert(helper: BaseViewHolder, item: PostInfo) {
        //处理大标题
        item.user?.let {
            val nick = if (it.gender == 0) mContext.getString(R.string.nick_man) else mContext.getString(R.string.nick_woman)
            val nickAdj = if (it.gender == 0) mContext.getString(R.string.nick_man_adj) else mContext.getString(R.string.nick_woman_adj)
            helper.setText(R.id.item_post_title, mContext.getString(R.string.post_item_title, nickAdj, nick, item.destination))
        }

        helper.setText(R.id.item_post_user_title, item.title ?: "一起去玩儿吧")
        helper.setText(R.id.item_post_content, item.content ?: "一起去玩儿吧")
        //图片处理
        if (item.imgs?.isNotEmpty() == true) {
            val jsonArray = JSONArray(item.imgs)
            val photo = helper.getView<ImageView>(R.id.item_post_photo)
            GlideApp.with(mContext).load(jsonArray[0]).into(photo)
        } else {
            //set a default picture
            helper.setImageResource(R.id.item_post_photo, R.drawable.item_def)
        }

    }
}