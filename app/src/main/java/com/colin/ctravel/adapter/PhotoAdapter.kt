package com.colin.ctravel.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.colin.ctravel.R
import com.colin.ctravel.photopicker.Image
import com.colin.ctravel.util.GlideApp

/**
 *create by colin 2018/9/14
 */
class PhotoAdapter(data: MutableList<Image>) : BaseQuickAdapter<Image, BaseViewHolder>(data) {

    init {
        mLayoutResId = R.layout.item_photo
    }

    override fun convert(helper: BaseViewHolder, item: Image) {
        val icon = helper.getView<ImageView>(R.id.item_photo)
        GlideApp.with(mContext).load(item.imagePath).into(icon)
    }


}