package com.colin.ctravel.util

import android.content.ContextWrapper
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import com.colin.ctravel.luban.Luban

/**
 *create by colin 2018/9/29
 *
 * 分享的工具类
 * 分享图片到各个平台。
 * 图片内容: 帖子标题，文字，一张图
 */
object ShareUtil {

    fun createPicture(bitmap: Bitmap, title: String, content: String): Bitmap {
        val result = Bitmap.createBitmap(1080, 1920, Bitmap.Config.RGB_565)
        val canvas = Canvas(result)
        val paint = Paint()
        val srcR = Rect(0, 0, bitmap.width, bitmap.height)
        val dstR = Rect(0, 0, 1080, 600)
        //绘制背景
        canvas.drawColor(Color.WHITE)
        //绘制图片
        canvas.drawBitmap(bitmap, srcR, dstR, paint)
        //绘制标题
        val textPaint = TextPaint()
        textPaint.color = Color.BLACK
        textPaint.style = Paint.Style.FILL
        textPaint.isAntiAlias = true
        textPaint.textSize = 48f
        canvas.translate(0f, (dstR.bottom + 100).toFloat())
        val titleLayout = StaticLayout(title, textPaint, 1080, Layout.Alignment.ALIGN_NORMAL, 1.5f, 0f, true)
        titleLayout.draw(canvas)

        //绘制内容
        canvas.translate(0f, 50f)
        val contentLayout = StaticLayout(content, textPaint, 1080, Layout.Alignment.ALIGN_NORMAL, 1.5f, 0f, true)
        contentLayout.draw(canvas)

        //绘制额外的文字内容
        return result
    }

    fun saveBitmap(bitmap: Bitmap) {

    }

    fun shareBitmap(contextWrapper: ContextWrapper, srcPic: Bitmap, title: String, content: String) {

    }

    fun shareText(text: String) {

    }

}