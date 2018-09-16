package com.colin.ctravel.activity

import android.Manifest
import com.colin.ctravel.R
import com.colin.ctravel.base.BaseActivity
import com.colin.ctravel.presenter.LoginPresenter
import com.colin.ctravel.presenter.imp.LoginPresenterImp
import com.colin.ctravel.util.createOrExistsDir
import com.colin.ctravel.util.jumpActivity
import com.colin.ctravel.util.photoCompressDirPath
import com.colin.ctravel.view.LoginView
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_login.*
import java.io.File

class LoginActivity : BaseActivity<LoginPresenter>(), LoginView {

    override fun setContentViewId(): Int {
        return R.layout.activity_login
    }

    override fun createPresenter(): LoginPresenter {
        return LoginPresenterImp(this)
    }

    override fun initView() {
        supportActionBar?.title = getString(R.string.login_title)
        //创建文件夹
        val permission = RxPermissions(this)
        permission.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe {
                    if (it) {
                        val photoCompress = File(photoCompressDirPath)
                        createOrExistsDir(photoCompress)//创建压缩缓存文件夹
                    }
                }
        login_btn_account?.setOnClickListener { _ ->
            //进行登陆检查
            login()
        }
    }

    private fun login() {
        val account = login_edit_account?.text.toString()
        val pwd = login_edit_pwd?.text.toString()
        if (account.isBlank()) {
            login_input_account?.error = "用户名为空"
            return
        }
        if (pwd.isBlank()) {
            login_input_account?.error = "密码为空"
            return
        }
        //登陆
        mPresenter?.login(account, pwd)
    }

    override fun loginSuccess() {
        jumpActivity(MainActivity::class.java, null)
        finish()
    }
}
