package com.colin.ctravel.net

/**
 * Created by tianweiping on 2017/12/11.
 */

interface NetConstant {
    companion object {
        //网络相关
        const val RESULT_CODE_TOKEN_ERROR = 301 //token失效
        const val RESULT_CODE_LOGINOTHER = 302 //其他地方登陆
        const val RESULT_CODE_SUCCESS = 200 //成功
        //        const val BASE_URL = "http://192.168.31.197:8089/" //公司电脑
        const val BASE_URL = "http://120.78.196.222:8080/ctravelapi/"  //测试环境
//        const val BASE_URL = "http://192.168.199.234:8089/"//家里电脑
    }
}
