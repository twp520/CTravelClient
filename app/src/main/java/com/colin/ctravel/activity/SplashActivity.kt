package com.colin.ctravel.activity

import android.Manifest
import android.animation.Animator
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.colin.ctravel.R
import com.colin.ctravel.module.TravelModule
import com.colin.ctravel.util.createOrExistsDir
import com.colin.ctravel.util.jumpActivity
import com.colin.ctravel.util.photoCompressDirPath
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_splash.*
import java.io.File

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //创建文件夹
        val permission = RxPermissions(this)
        permission.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe {
                    if (it) {
                        val photoCompress = File(photoCompressDirPath)
                        createOrExistsDir(photoCompress)//创建压缩缓存文件夹
                        startLogin()
                    } else {
                        //TODO 无法使用
                        AlertDialog.Builder(this)
                                .setTitle("提示")
                                .setMessage("请打开存储权限，否则应用功能无法正常使用。")
                                .setPositiveButton("确定") { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .create()
                                .show()
                    }
                }
    }

    //判断是否有token，有就直接跳转，没有就去登陆
    private fun startLogin() {
        val token = TravelModule.getToken()
        val target = if (token.isBlank()) {
            LoginActivity::class.java
        } else {

            MainActivity::class.java
        }
        splash_bg.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
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
