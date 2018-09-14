package com.colin.ctravel.photopicker

import android.widget.ImageView
import com.bumptech.glide.load.DecodeFormat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.colin.ctravel.R
import com.colin.ctravel.util.GlideApp

/**
 *create by colin 2018/9/14
 */
class ImageAdapter(data: MutableList<Image>) : BaseQuickAdapter<Image, BaseViewHolder>(data) {

    private var mAllAlbum: Album? = null

    init {
        mLayoutResId = R.layout.item_picker
    }

    override fun convert(helper: BaseViewHolder, item: Image) {
        val photo = helper.getView<ImageView>(R.id.item_picker_photo)
        GlideApp.with(mContext)
                .load(item.imagePath)
                .thumbnail(0.25f)
                .format(DecodeFormat.PREFER_RGB_565)
                .into(photo)
        helper.setChecked(R.id.item_picker_ckb, item.isChecked)
        helper.addOnClickListener(R.id.item_picker_ckb)
    }

    fun setAllAlbum(album: Album) {
        this.mAllAlbum = album
    }

    fun checkedInAll(item: Image) {
        val index = mAllAlbum?.images?.indexOf(item)
        if (index != null && index != -1) {
            mAllAlbum?.images?.get(index)?.isChecked = item.isChecked
        }
    }

    fun getCheckedCount(): Int {
        return mAllAlbum?.images?.count {
            it.isChecked
        } ?: 0
    }

    fun getCheckedImage(): ArrayList<Image> {
        val result: ArrayList<Image> = arrayListOf()
        mAllAlbum?.images?.forEach {
            if (it.isChecked)
                result.add(it)
        }
        return result
    }
}