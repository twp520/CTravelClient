package com.colin.ctravel.net

import com.colin.ctravel.BuildConfig
import com.colin.ctravel.module.TravelModule
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * create by colin
 */
object BuildAPI {
    private var headerRetrofit: Retrofit? = null

    /**
     * 添加自定义header
     *
     * @return 自定义的OkHttpClient
     */
    private val tokenClient: OkHttpClient
        get() = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("Accept", "application/json;charset=UTF-8")
                            .addHeader("token", TravelModule.getToken())
                            .build()
                    chain.proceed(request)
                }
                .addNetworkInterceptor(HttpLogInterceptor("httpLog", BuildConfig.DEBUG))
//                .addInterceptor(TokenInterceptor())
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

    /**
     *  获取Servers
     * @return APIServers
     */
    fun getAPISevers(): APIServers {
        return buildAPISevers(APIServers::class.java)
    }

    /**
     * 构造有自定义header字段token的Retrofit
     *
     * @return APIServers
     */
    private fun <T> buildAPISevers(service: Class<T>): T {
        if (headerRetrofit == null) {
            headerRetrofit = Retrofit.Builder().baseUrl(NetConstant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(tokenClient)
                    .build()
        }
        return headerRetrofit!!.create(service)
    }

    fun clear() {
        headerRetrofit = null
    }


}
