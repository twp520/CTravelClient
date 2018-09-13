package com.colin.ctravel.net

import com.colin.ctravel.base.BaseResultBean
import com.colin.ctravel.bean.PostInfo
import com.colin.ctravel.bean.User
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * create by colin
 */
interface APIServers {

    @GET("/user/test")
    fun test(): Observable<BaseResultBean<String>>


    @POST("/user/login")
    @FormUrlEncoded
    fun login(@Field("account") account: String, @Field("passworld") pwd: String): Observable<BaseResultBean<User>>


    @GET("/post/getPosts")
    fun getAllPost(): Observable<BaseResultBean<MutableList<PostInfo>>>
}
