package com.colin.ctravel.util

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

//@Excludes(com.colin.picklib.CPickerGlideModule)
@GlideModule
class MyGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        val options = RequestOptions()
        builder.setDefaultRequestOptions(options.format(DecodeFormat.PREFER_RGB_565))
    }

}
