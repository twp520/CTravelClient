package com.colin.ctravel.photopicker

import com.chad.library.adapter.base.entity.MultiItemEntity

class ImageTakePhoto : MultiItemEntity {
    override fun getItemType(): Int {
        return 1
    }
}