package com.colin.ctravel

import android.app.Application
import com.socks.library.KLog

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        KLog.init(BuildConfig.DEBUG, "travel")
    }
}