package com.colin.ctravel.photopicker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.colin.ctravel.R
import com.colin.ctravel.util.GlideApp
import kotlinx.android.synthetic.main.activity_large.*

class LargeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_large)
        if (intent.extras == null)
            return
        val path = intent.extras.getString("path")
        GlideApp.with(this).load(path).into(large_photo)
    }
}
