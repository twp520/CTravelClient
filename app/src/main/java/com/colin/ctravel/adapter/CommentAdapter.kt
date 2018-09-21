package com.colin.ctravel.adapter

import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.colin.ctravel.R
import com.colin.ctravel.bean.Comment
import com.colin.ctravel.util.GlideApp
import com.colin.ctravel.widget.CenterAlignImageSpan

/**
 *create by colin 2018/9/21
 */
class CommentAdapter(data: MutableList<Comment>) : BaseQuickAdapter<Comment, BaseViewHolder>(data) {

    init {
        mLayoutResId = R.layout.item_comment
    }


    override fun convert(helper: BaseViewHolder, item: Comment) {
        val user = item.ownUser
        var builder = SpannableStringBuilder()
        if (user != null) {
            GlideApp.with(mContext).load(item.ownUser?.headUrl)
                    .optionalCircleCrop()
                    .into(object : SimpleTarget<Drawable>() {
                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            resource.setBounds(0, 0, 50, 50)
                            builder = SpannableStringBuilder()
                            builder.append("头像")
                            builder.setSpan(CenterAlignImageSpan(resource),
                                    0, builder.length, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
                            builder.append("  ")
                            val startIndex = builder.length
                            builder.append(user.nickname).append(" : ")
                            builder.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary)),
                                    startIndex, builder.length, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
                            builder.append(item.content)
                            helper.setText(R.id.item_comment_tv, builder)
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            builder.append(user.nickname).append(" : ")
                            builder.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary)),
                                    0, builder.length, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
                            builder.append(item.content)
                            helper.setText(R.id.item_comment_tv, builder)
                        }
                    })

        } else {
            helper.setText(R.id.item_comment_tv, "留言人不知道去哪儿了")
        }

    }
}