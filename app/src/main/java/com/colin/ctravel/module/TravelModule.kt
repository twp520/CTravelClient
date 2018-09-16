package com.colin.ctravel.module

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.colin.ctravel.bean.PostInfo
import com.colin.ctravel.bean.User
import com.colin.ctravel.db.AppDataBase
import com.colin.ctravel.luban.Luban
import com.colin.ctravel.net.BuildAPI
import com.colin.ctravel.net.HandResultFunc
import com.colin.ctravel.photopicker.Image
import com.colin.ctravel.util.LOG_TAG
import com.colin.ctravel.util.OSSManager
import com.colin.ctravel.util.SPUtils
import com.colin.ctravel.util.photoCompressDirPath
import com.socks.library.KLog
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import java.io.File

object TravelModule {
    private var cacheMap: HashMap<String, Any> = hashMapOf()

    fun test() {
        BuildAPI.getAPISevers()
                .test()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    KLog.e(LOG_TAG, "请求成功")
                }, {
                    it.printStackTrace()
                    KLog.e(LOG_TAG, "请求失败")
                })
    }

    /**
     * 登陆
     * @param  account 用户名
     * @param pwd 密码
     * @return 用户信息
     */
    fun login(account: String, pwd: String): Observable<User> {

        return BuildAPI.getAPISevers()
                .login(account, pwd)
                .map(HandResultFunc())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 获取所有的帖子
     * @return 帖子列表
     */
    fun getAllPost(): Observable<MutableList<PostInfo>> {

        return BuildAPI.getAPISevers()
                .getAllPost().map(HandResultFunc())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 同步上传单张图片到阿里云OSS
     * @param file 图片文件
     * @return 图片地址
     */
    private fun upLoadPhoto(file: File): Flowable<String> {
        return Flowable.just(OSSManager.upLoadFileSync(file)).subscribeOn(Schedulers.io())
    }

    /**
     * 上传多张图片到阿里云OSS
     * @param images 图片集合
     * @return 多张图片地址的json字符串
     */
    fun upLoadPhoto(images: MutableList<Image>, context: Context?): LiveData<String> {
        val imgArray = JSONArray()

        val liveData: MutableLiveData<String> = MutableLiveData()
        Flowable.fromIterable(images)
                .map {
                    //单个压缩图片，返回list包装的集合
                    Luban.with(context)
                            .load(it.imageFile)
                            .setTargetDir(photoCompressDirPath)
                            .get()
                }
                .subscribeOn(Schedulers.io())
                .flatMap {
                    //迭代上传
                    Flowable.fromIterable(it)
                }
                .subscribeOn(Schedulers.io())
                .flatMap {
                    //上传单个文件
                    upLoadPhoto(it)
                }.subscribe({
                    imgArray.put(it) //放入单个图片地址
                }, {
                    //发生错误
                    it.printStackTrace()
                    liveData.postValue("result_fail")
                }, {
                    //完成全部上传
                    liveData.postValue(imgArray.toString())
                })
        return liveData
    }

    fun sendPost(map: HashMap<String, Any>): Observable<String> {
        return BuildAPI.getAPISevers().sendPost(map)
                .map(HandResultFunc())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * 缓存用户信息
     * @param  user 用户信息
     */
    fun saveUser(user: User, context: Context) {
        cacheMap["user"] = user
        //固化用户信息
        Observable.create<Long> {
            val row = AppDataBase.getInstance(context).userDao().insertUser(user)
            if (row == 1L) {
                it.onNext(row)
                it.onComplete()
            } else it.onError(NullPointerException("数据库插入异常"))
        }.subscribeOn(Schedulers.io())
                .subscribe()
        SPUtils.getInstance().put("token", user.token)
        SPUtils.getInstance().put("uid", user.id)
    }

    /**
     * 获取缓存的用户信息
     *<p>
     * 如果没有则返回NULL
     * @return 用户信息
     */
    fun getUser(context: Context): Observable<User>? {
        val user = cacheMap["user"]
        if (user != null && user is User) {
            return Observable.just(user)
        }
        val uid = getUserId()
        if (uid != -1) {
            return Observable.create<User> {
                val dbUser = AppDataBase.getInstance(context)
                        .userDao()
                        .getLoginUser(uid)
                it.onNext(dbUser)
                it.onComplete()
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
        return null
    }

    fun getUserId(): Int {
        return SPUtils.getInstance().getInt("uid", -1)
    }

    fun getToken(): String {
        return SPUtils.getInstance().getString("token")
    }
}