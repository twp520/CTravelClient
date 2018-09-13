package com.colin.ctravel.module

import com.colin.ctravel.bean.PostInfo
import com.colin.ctravel.bean.User
import com.colin.ctravel.net.BuildAPI
import com.colin.ctravel.net.HandResultFunc
import com.colin.ctravel.util.LOG_TAG
import com.socks.library.KLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
     * 缓存用户信息
     * @param  user 用户信息
     */
    fun saveUser(user: User) {
        cacheMap["user"] = user
    }

    /**
     * 获取缓存的用户信息
     *<p>
     * 如果没有则返回NULL
     * @return 用户信息
     */
    fun getUser(): User? {
        val user = cacheMap["user"]
        if (user != null && user is User)
            return user
        return null
    }
}