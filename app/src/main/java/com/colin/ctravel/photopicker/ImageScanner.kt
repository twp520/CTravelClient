package com.colin.ctravel.photopicker

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.ContentResolver
import android.provider.MediaStore

/**
 *create by colin 2018/9/14
 *
 * 图片扫描器
 */

//查询的uri
private val EXTERNAL_IMAGES_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//查询的字段
private val ALBUM_PROJECTION = arrayOf(MediaStore.Images.Media.BUCKET_ID, //相册ID
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,//相册名字
        MediaStore.Images.Media._ID,//图片ID
        MediaStore.Images.Media.DATA)//图片地址

//各自列的序号
private const val BUCKET_ID = 0
private const val BUCKET_DISPLAY_NAME = 1
private const val IMAGE_ID = 2
private const val DATA = 3

//大小阈值
private const val ALBUM_SELECTION = "_size> 20000"


class ImageScanner(private var mContentResolver: ContentResolver) {

    fun getImageAlbum(): LiveData<MutableList<Album>> {
        val liveData = MutableLiveData<MutableList<Album>>()
        Thread {
            val albumList = mutableListOf<Album>()
            val albumKes: HashSet<String> = hashSetOf()
            val cursor = mContentResolver.query(EXTERNAL_IMAGES_URI,
                    ALBUM_PROJECTION, ALBUM_SELECTION, null, null)
            val allAlbum = Album("所有图片")
            albumList.add(allAlbum)
            var album: Album? = null
            var bucketId: String?
            var image: Image?
            try {
                while (cursor.moveToNext()) {
                    image = Image(cursor.getInt(IMAGE_ID))
                    image.imageFile = cursor.getString(DATA)
                    image.imagePath = "file://" + cursor.getString(DATA)//供图片加载框架使用的路径
                    bucketId = cursor.getString(BUCKET_ID)
                    allAlbum.images.add(image)
                    if (!albumKes.contains(bucketId)) { //新的相册
                        album = Album()
                        album.name = cursor.getString(BUCKET_DISPLAY_NAME)
                        album.images.add(image)
                        albumList.add(album)
                        albumKes.add(bucketId)
                    } else { //同一个相册
                        album?.images?.add(image)
                    }
                }
            } finally {
                cursor.close()
                liveData.postValue(albumList)
            }
        }.start()
        return liveData
    }

}