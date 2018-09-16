package com.colin.ctravel.photopicker

import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.MultipleItemRvAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity

class ImageAdapterM(data: MutableList<MultiItemEntity>) : MultipleItemRvAdapter<MultiItemEntity, BaseViewHolder>(data) {

    //图片类型是 0，加号类型是1
    private var mAllAlbum: Album? = null

    init {
        finishInitialize()
    }

    override fun registerItemProvider() {
        mProviderDelegate.registerProvider(ImageProvder())
        mProviderDelegate.registerProvider(TakePhotoProvder())
    }

    override fun getViewType(t: MultiItemEntity): Int {
        return t.itemType
    }

    override fun replaceData(data: MutableCollection<out MultiItemEntity>) {
        // 不是同一个引用才清空列表
        if (data !== mData) {
            mData.removeAll {
                it.itemType == 0
            }
            mData.addAll(data)
        }
        notifyDataSetChanged()
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