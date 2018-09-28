package com.colin.ctravel.module

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.colin.ctravel.bean.Comment
import com.colin.ctravel.bean.PostInfo
import com.colin.ctravel.bean.User
import com.colin.ctravel.db.AppDataBase
import com.colin.ctravel.luban.Luban
import com.colin.ctravel.net.BuildAPI
import com.colin.ctravel.net.HandResultFunc
import com.colin.ctravel.util.LOG_TAG
import com.colin.ctravel.util.OSSManager
import com.colin.ctravel.util.SPUtils
import com.colin.ctravel.util.photoCompressDirPath
import com.colin.picklib.Image
import com.socks.library.KLog
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
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
    }

    fun register(account: String, pwd: String, nickname: String, gender: Int, headUrl: String): Observable<User> {
        val map = hashMapOf<String, Any>()
        map["account"] = account
        map["passworld"] = pwd
        map["nickname"] = nickname
        map["headUrl"] = headUrl
        map["gender"] = gender
        map["fromWx"] = false
        return BuildAPI.getAPISevers()
                .register(map)
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
     * 上传单张图片到阿里云OSS
     * @param image 图片
     * @return 图片地址
     */
    fun upLoadPhoto(image: Image): Observable<String> {
        return Observable.create(ObservableOnSubscribe<String> {
            val add = OSSManager.upLoadFileSync(File(image.imageFile))
            it.onNext(add)
            it.onComplete()
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 上传单张图片到阿里云OSS
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
    fun upLoadPhotoList(images: MutableList<Image>, context: Context?): LiveData<String> {
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

    /**
     * 发送post
     * @param map 参数集合
     * @return 结果
     */
    fun sendPost(map: HashMap<String, Any>): Observable<String> {
        return BuildAPI.getAPISevers().sendPost(map)
                .map(HandResultFunc())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 发送评论
     * @param content 评论内容
     * @param postId 被评论的post 的id
     * @return 当前评论实体类
     */
    fun sendComment(content: String, postId: Int): Observable<Comment> {
        val map = hashMapOf<String, Any>()
        map["content"] = content
        map["postId"] = postId
        map["atUid"] = -1
        return BuildAPI.getAPISevers()
                .sendComment(map)
                .map(HandResultFunc())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 获得一个帖子的评论
     * @param postId  帖子 ID
     * @return 评论列表
     */
    fun getPostComment(postId: Int): Observable<MutableList<Comment>> {
        return BuildAPI.getAPISevers()
                .getCommentByPostId(postId)
                .map(HandResultFunc())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 收藏一个帖子
     * @param postId 帖子ID
     * @return 结果
     */
    fun favoritePost(postId: Int): Observable<String> {
        return BuildAPI.getAPISevers()
                .favoritePost(postId)
                .map(HandResultFunc())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 获取用户的收藏列表
     * @return 收藏列表
     */
    fun getUserFavoritePost(): Observable<MutableList<PostInfo>> {
        return BuildAPI.getAPISevers()
                .getUserFavoritePost()
                .map(HandResultFunc())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 缓存用户信息
     * @param  user 用户信息
     */
    fun saveUser(user: User, context: Context): Observable<Int> {
        cacheMap["user"] = user
        //固化用户信息
        SPUtils.getInstance().put("token", user.token)
        SPUtils.getInstance().put("uid", user.id)
        return Observable.create<Int> {
            val dao = AppDataBase.getInstance(context.applicationContext).userDao()
            val dbUser = dao.selectUserById(user.id)
            val row = if (dbUser != null) {
                dao.updateUser(user)
            } else {
                dao.insertUser(user).toInt()
            }
            if (row != -1) {
                it.onNext(row)
                it.onComplete()
            } else {
                it.onError(NullPointerException("插入数据库异常"))
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 获取缓存的用户信息
     *<p>
     * 如果没有则返回NULL
     * @return 用户信息
     */
    fun getUser(context: Context): Observable<User?>? {
        val user = cacheMap["user"]
        if (user != null && user is User) {
            return Observable.just(user)
        }
        val uid = getUserId()
        if (uid != -1) {
            return Observable.create<User?> {
                val dbUser = AppDataBase.getInstance(context)
                        .userDao()
                        .selectUserById(uid)
                if (dbUser != null) {
                    it.onNext(dbUser)
                    it.onComplete()
                } else {
                    it.onError(NullPointerException("数据库查询为空"))
                }
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