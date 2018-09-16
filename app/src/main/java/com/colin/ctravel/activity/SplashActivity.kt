package com.colin.ctravel.activity

import android.animation.Animator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.colin.ctravel.R
import com.colin.ctravel.module.TravelModule
import com.colin.ctravel.util.jumpActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //判断是否有token，有就直接跳转，没有就去登陆
        val token = TravelModule.getToken()
        val target = if (token.isBlank()) {
            LoginActivity::class.java
        } else {

            MainActivity::class.java
        }
        splash_bg.animate()
                .scaleX(1.5f)
                .scaleY(1.5f   )
                .setDuration(1500)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationEnd(animation: Animator?) {
                        jumpActivity(target)
                        finish()
                    }

                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationRepeat(animation: Animator?) {}
                    override fun onAnimationStart(animation: Animator?) {}
                })
                .setStartDelay(1000)
                .start()
    }
}
