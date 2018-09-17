package com.colin.ctravel.net

import com.colin.ctravel.base.BaseResultBean
import com.colin.ctravel.bean.PostInfo
import com.colin.ctravel.bean.User
import io.reactivex.Observable
import retrofit2.http.*

/**
 * create by colin
 */
interface APIServers {

    @GET("/user/test")
    fun test(): Observable<BaseResultBean<String>>


    @POST("/user/login")
    @FormUrlEncoded
    fun login(@Field("account") account: String, @Field("passworld") pwd: String): Observable<BaseResultBean<User>>

    @POST("/user/register")
    @FormUrlEncoded
    fun register(@FieldMap map: HashMap<String, Any>): Observable<BaseResultBean<User>>

    @GET("/post/getPosts")
    fun getAllPost(): Observable<BaseResultBean<MutableList<PostInfo>>>

    @POST("/post/sendPost")
    @FormUrlEncoded
    fun sendPost(@FieldMap map: HashMap<String, Any>): Observable<BaseResultBean<String>>
}
