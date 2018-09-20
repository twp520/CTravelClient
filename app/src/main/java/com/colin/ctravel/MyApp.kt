package com.colin.ctravel

import android.app.Application
import com.colin.ctravel.util.OSSManager
import com.colin.ctravel.util.SPUtils
import com.socks.library.KLog
import com.tencent.bugly.Bugly

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        KLog.init(BuildConfig.DEBUG, "travel")
        OSSManager.init(this)
        SPUtils.setApp(this)
        Bugly.init(this, "f916c61e43", true)
    }

}