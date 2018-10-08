package com.colin.ctravel.util

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.support.v4.app.ShareCompat
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

/**
 *create by colin 2018/9/29
 *
 * 分享的工具类
 * 分享图片到各个平台。
 * 图片内容: 帖子标题，文字，一张图
 */
object ShareUtil {

    private fun createPicture(bitmap: Bitmap, title: String, content: String): Bitmap {
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
        canvas.translate(0f, 100f)
        val contentLayout = StaticLayout("$content \n\n\n\n各大应用商店搜索《结伴同游》了解更多", textPaint, 1080, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0f, true)
        contentLayout.draw(canvas)
        //绘制额外的文字内容

        return result
    }


    //分享图片，需要请求存储卡权限
    fun shareBitmap(activity: Activity, srcPic: Bitmap, title: String, content: String): Observable<Intent> {
        return Observable.create<Intent> {
            try {
                val sharePic = createPicture(srcPic, title, content)
                //保存到本地
                val file = File(activity.externalCacheDir, "ctravel_share_pic${TimeUtils.getNowString()}.jpeg")
                val success = createOrExistsFile(file)
                if (success) {
                    val fos = FileOutputStream(file)
                    sharePic.compress(Bitmap.CompressFormat.JPEG, 50, fos)
                    fos.flush()
                    fos.close()
                }
                val shareInten = ShareCompat.IntentBuilder.from(activity)
                        .setType("image/*")
                        .addStream(getUriFromFile(activity, file))
                        .setChooserTitle("分享到")
                        .createChooserIntent()

                it.onNext(shareInten)
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


}